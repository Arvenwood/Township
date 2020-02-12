package arvenwood.towns.plugin

import arvenwood.towns.api.claim.Claim
import arvenwood.towns.api.claim.ClaimService
import arvenwood.towns.api.invite.Invite
import arvenwood.towns.api.invite.InviteService
import arvenwood.towns.api.resident.ResidentService
import arvenwood.towns.api.town.Town
import arvenwood.towns.api.town.TownService
import arvenwood.towns.plugin.claim.ClaimImpl
import arvenwood.towns.plugin.claim.ClaimServiceImpl
import arvenwood.towns.plugin.command.CommandTown
import arvenwood.towns.plugin.invite.InviteImpl
import arvenwood.towns.plugin.invite.InviteServiceImpl
import arvenwood.towns.plugin.listener.BlockListener
import arvenwood.towns.plugin.listener.ConnectionListener
import arvenwood.towns.plugin.resident.ResidentServiceImpl
import arvenwood.towns.plugin.town.TownImpl
import arvenwood.towns.plugin.town.TownServiceImpl
import com.google.inject.Inject
import org.slf4j.Logger
import org.spongepowered.api.Sponge
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameInitializationEvent
import org.spongepowered.api.plugin.Plugin

@Plugin(id = "arven-towns", name = "ArvenTowns", version = "0.3.0")
class ArvenTowns @Inject constructor(private val logger: Logger) {

    @Listener
    fun onInit(event: GameInitializationEvent) {
        Sponge.getRegistry().registerBuilderSupplier(Claim.Builder::class.java, ClaimImpl::Builder)
        Sponge.getRegistry().registerBuilderSupplier(Invite.Builder::class.java, InviteImpl::Builder)
        Sponge.getRegistry().registerBuilderSupplier(Town.Builder::class.java, TownImpl::Builder)

        this.logger.info("Registering services...")

        Sponge.getServiceManager().setProvider(this, ClaimService::class.java, ClaimServiceImpl())
        Sponge.getServiceManager().setProvider(this, InviteService::class.java, InviteServiceImpl())
        Sponge.getServiceManager().setProvider(this, ResidentService::class.java, ResidentServiceImpl())
        Sponge.getServiceManager().setProvider(this, TownService::class.java, TownServiceImpl())

        this.logger.info("Registering listeners...")

        Sponge.getEventManager().registerListeners(this, ConnectionListener())
        Sponge.getEventManager().registerListeners(this, BlockListener())

        this.logger.info("Registering commands...")

        Sponge.getCommandManager().register(this, CommandTown.SPEC, "town", "t")
    }
}