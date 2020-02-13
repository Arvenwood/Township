package arvenwood.towns.plugin.util

import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.resident.ResidentService
import org.spongepowered.api.data.DataQuery
import org.spongepowered.api.data.DataView
import java.util.*

fun DataView.getUUID(path: DataQuery): Optional<UUID> =
    this.getString(path).map(UUID::fromString)

fun DataView.getUUIDList(path: DataQuery): Optional<List<UUID>> =
    this.getStringList(path).map { it.map(UUID::fromString) }

fun DataView.getResidentByUUID(path: DataQuery): Optional<Resident> =
    this.getUUID(path).flatMap { ResidentService.getInstance().getResident(it) }

fun DataView.getResidentsByUUIDList(path: DataQuery): Optional<List<Resident>> =
    this.getUUIDList(path).map { residents: List<UUID> ->
        residents.map {
            ResidentService.getInstance().getResident(it).get()
        }
    }