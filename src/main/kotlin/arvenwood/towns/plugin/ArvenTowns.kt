package arvenwood.towns.plugin

import arvenwood.towns.api.claim.Claim
import arvenwood.towns.api.claim.ClaimService
import arvenwood.towns.api.resident.ResidentService
import arvenwood.towns.api.town.Town
import arvenwood.towns.api.town.TownService
import arvenwood.towns.plugin.claim.SimpleClaim
import arvenwood.towns.plugin.claim.SimpleClaimService
import arvenwood.towns.plugin.command.CommandTown
import arvenwood.towns.plugin.listener.BlockListener
import arvenwood.towns.plugin.listener.ConnectionListener
import arvenwood.towns.plugin.resident.SimpleResidentService
import arvenwood.towns.plugin.town.SimpleTown
import arvenwood.towns.plugin.town.SimpleTownService
import com.google.inject.Inject
import org.slf4j.Logger
import org.spongepowered.api.Sponge
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameInitializationEvent
import org.spongepowered.api.plugin.Plugin

@Plugin(id = "arven-towns", name = "ArvenTowns", version = "0.1.0")
class ArvenTowns @Inject constructor(private val logger: Logger) {

    @Listener
    fun onInit(event: GameInitializationEvent) {
        Sponge.getRegistry().registerBuilderSupplier(Claim.Builder::class.java, SimpleClaim::Builder)
        Sponge.getRegistry().registerBuilderSupplier(Town.Builder::class.java, SimpleTown::Builder)

        this.logger.info("Registering services...")

        Sponge.getServiceManager().setProvider(this, ClaimService::class.java, SimpleClaimService())
        Sponge.getServiceManager().setProvider(this, ResidentService::class.java, SimpleResidentService())
        Sponge.getServiceManager().setProvider(this, TownService::class.java, SimpleTownService())

        this.logger.info("Registering listeners...")

        Sponge.getEventManager().registerListeners(this, ConnectionListener())
        Sponge.getEventManager().registerListeners(this, BlockListener())

        this.logger.info("Registering commands...")

        Sponge.getCommandManager().register(this, CommandTown.SPEC, "town", "t")
    }
}