package arvenwood.towns.plugin

import arvenwood.towns.api.claim.Claim
import arvenwood.towns.api.claim.ClaimService
import arvenwood.towns.api.invite.Invite
import arvenwood.towns.api.invite.InviteService
import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.resident.ResidentService
import arvenwood.towns.api.town.Town
import arvenwood.towns.api.town.TownService
import arvenwood.towns.api.warp.Warp
import arvenwood.towns.api.warp.WarpService
import arvenwood.towns.plugin.claim.ClaimDataBuilder
import arvenwood.towns.plugin.claim.ClaimImpl
import arvenwood.towns.plugin.claim.ClaimServiceImpl
import arvenwood.towns.plugin.command.CommandTown
import arvenwood.towns.plugin.invite.InviteImpl
import arvenwood.towns.plugin.invite.InviteServiceImpl
import arvenwood.towns.plugin.listener.BlockListener
import arvenwood.towns.plugin.listener.ConnectionListener
import arvenwood.towns.plugin.resident.ResidentDataBuilder
import arvenwood.towns.plugin.resident.ResidentServiceImpl
import arvenwood.towns.plugin.storage.DataLoader
import arvenwood.towns.plugin.storage.FileDataLoader
import arvenwood.towns.plugin.town.TownDataBuilder
import arvenwood.towns.plugin.town.TownImpl
import arvenwood.towns.plugin.town.TownServiceImpl
import arvenwood.towns.plugin.util.setProvider
import arvenwood.towns.plugin.warp.WarpDataBuilder
import arvenwood.towns.plugin.warp.WarpImpl
import arvenwood.towns.plugin.warp.WarpServiceImpl
import com.google.inject.Inject
import org.slf4j.Logger
import org.spongepowered.api.Sponge
import org.spongepowered.api.config.ConfigDir
import org.spongepowered.api.data.persistence.DataFormats
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.*
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.scheduler.Task
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

@Plugin(id = "arven-towns", name = "ArvenTowns", version = "0.5.0")
class ArvenTowns @Inject constructor(
    private val logger: Logger,
    @ConfigDir(sharedRoot = false) private val configDir: Path
) {

    private lateinit var dataLoader: DataLoader

    private lateinit var claimService: ClaimServiceImpl
    private lateinit var inviteService: InviteServiceImpl
    private lateinit var residentService: ResidentServiceImpl
    private lateinit var townService: TownServiceImpl
    private lateinit var warpService: WarpServiceImpl

    private var saveTask: Task? = null

    @Listener
    fun onPreInit(event: GamePreInitializationEvent) {
        this.dataLoader = FileDataLoader(Paths.get("arven-towns"), DataFormats.NBT)

        this.claimService = ClaimServiceImpl()
        this.inviteService = InviteServiceImpl()
        this.residentService = ResidentServiceImpl()
        this.townService = TownServiceImpl()
        this.warpService = WarpServiceImpl()
    }

    @Listener
    fun onInit(event: GameInitializationEvent) {
        this.logger.info("Registering data builders...")

        Sponge.getDataManager().registerBuilder(Claim::class.java, ClaimDataBuilder())
        Sponge.getDataManager().registerBuilder(Resident::class.java, ResidentDataBuilder())
        Sponge.getDataManager().registerBuilder(Town::class.java, TownDataBuilder())
        Sponge.getDataManager().registerBuilder(Warp::class.java, WarpDataBuilder())

        this.logger.info("Registering catalog builders...")

        Sponge.getRegistry()
            .registerBuilderSupplier(Claim.Builder::class.java, ClaimImpl::Builder)
            .registerBuilderSupplier(Invite.Builder::class.java, InviteImpl::Builder)
            .registerBuilderSupplier(Town.Builder::class.java, TownImpl::Builder)
            .registerBuilderSupplier(Warp.Builder::class.java, WarpImpl::Builder)

        this.logger.info("Registering services...")

        Sponge.getServiceManager()
            .setProvider<ClaimService>(this, this.claimService)
            .setProvider<InviteService>(this, this.inviteService)
            .setProvider<ResidentService>(this, this.residentService)
            .setProvider<TownService>(this, this.townService)
            .setProvider<WarpService>(this, this.warpService)

        this.logger.info("Registering listeners...")

        Sponge.getEventManager().registerListeners(this, ConnectionListener())
        Sponge.getEventManager().registerListeners(this, BlockListener())

        this.logger.info("Registering commands...")

        Sponge.getCommandManager().register(this, CommandTown.SPEC, "town", "t")
    }

    @Listener
    fun onStarted(event: GameStartedServerEvent) {
        this.loadPluginData()

        Sponge.getScheduler().createTaskBuilder()
            .interval(5, TimeUnit.MINUTES)
            .execute { task: Task ->
                this.saveTask = task
                this.savePluginData()
            }
            .name("PeriodicSaveTask")
            .submit(this)
    }

    @Listener
    fun onStopping(event: GameStoppingServerEvent) {
        this.saveTask?.cancel()
        this.saveTask = null

        this.savePluginData()
    }

    private fun loadPluginData() {
        this.logger.info("Loading residents...")

        this.residentService.load(this.dataLoader)

        this.logger.info("Loading towns...")

        this.townService.load(this.dataLoader)

        this.logger.info("Loading claims...")

        this.claimService.load(this.dataLoader)

        this.logger.info("Loading warps...")

        this.warpService.load(this.dataLoader)
    }

    private fun savePluginData() {
        this.logger.info("Saving residents...")

        this.residentService.save(this.dataLoader)

        this.logger.info("Saving towns...")

        this.townService.save(this.dataLoader)

        this.logger.info("Saving claims...")

        this.claimService.save(this.dataLoader)

        this.logger.info("Saving warps...")

        this.warpService.save(this.dataLoader)
    }
}