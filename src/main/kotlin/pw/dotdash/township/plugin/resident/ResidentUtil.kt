package pw.dotdash.township.plugin.resident

import pw.dotdash.township.api.resident.Resident
import pw.dotdash.township.api.resident.ResidentService
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.entity.living.player.Player

fun CommandSource.getPlayerOrSystemResident(): Resident {
    val service: ResidentService = ResidentService.getInstance()
    return if (this is Player) service.getOrCreateResident(this) else service.systemResident
}