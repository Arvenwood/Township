package arvenwood.towns.plugin.resident

import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.resident.ResidentService
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.entity.living.player.Player

fun CommandSource.getPlayerOrSystemResident(): Resident {
    val service: ResidentService = ResidentService.getInstance()
    return if (this is Player) service.getOrCreateResident(this) else service.systemResident
}