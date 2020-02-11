package arvenwood.towns.plugin.listener

import arvenwood.towns.api.resident.ResidentService
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.network.ClientConnectionEvent

class ConnectionListener {

    @Listener
    fun onJoin(event: ClientConnectionEvent.Join) {
        ResidentService.get().getOrCreateResident(event.targetEntity)
    }

    @Listener
    fun onDisconnect(event: ClientConnectionEvent.Disconnect) {

    }
}