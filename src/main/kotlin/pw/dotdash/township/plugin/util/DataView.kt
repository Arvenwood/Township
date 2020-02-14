package pw.dotdash.township.plugin.util

import com.google.common.collect.Table
import com.google.common.collect.Tables
import org.spongepowered.api.Sponge
import org.spongepowered.api.data.DataContainer
import pw.dotdash.township.api.resident.Resident
import pw.dotdash.township.api.resident.ResidentService
import org.spongepowered.api.data.DataQuery
import org.spongepowered.api.data.DataView
import pw.dotdash.township.api.permission.ClaimPermission
import java.util.*

fun DataView.getUUID(path: DataQuery): Optional<UUID> =
    this.getObject(path, UUID::class.java)

fun DataView.getUUIDList(path: DataQuery): Optional<List<UUID>> =
    this.getObjectList(path, UUID::class.java)

fun DataView.getResidentByUUID(path: DataQuery): Optional<Resident> =
    this.getUUID(path).flatMap { ResidentService.getInstance().getResident(it) }

fun DataView.getResidentsByUUIDList(path: DataQuery): Optional<List<Resident>> =
    this.getUUIDList(path).map { residents: List<UUID> ->
        residents.map {
            ResidentService.getInstance().getResident(it).get()
        }
    }

fun DataView.getClaimPermissions(path: DataQuery): Optional<Map<ClaimPermission, Boolean>> =
    this.getView(path).map { sub: DataView ->
        val result = HashMap<ClaimPermission, Boolean>()

        for (permissionKey: DataQuery in sub.getKeys(false)) {
            val permission: ClaimPermission = Sponge.getRegistry()
                .getType(ClaimPermission::class.java, permissionKey.toString())
                .unwrap() ?: continue
            val value: Boolean = sub.getBoolean(permissionKey).get()

            result[permission] = value
        }

        result
    }

fun DataView.getRolePermissionOverrides(): Table<UUID, ClaimPermission, Boolean> {
    val result: Table<UUID, ClaimPermission, Boolean> =
        Tables.newCustomTable(HashMap()) { HashMap<ClaimPermission, Boolean>() }

    for (roleKey: DataQuery in this.getKeys(false)) {
        val role: UUID = UUID.fromString(roleKey.toString())
        val subView: DataView = this.getView(roleKey).get()

        for (permissionKey: DataQuery in subView.getKeys(false)) {
            val permission: ClaimPermission = Sponge.getRegistry()
                .getType(ClaimPermission::class.java, permissionKey.toString())
                .unwrap() ?: continue
            val value: Boolean = subView.getBoolean(permissionKey).get()

            result[role, permission] = value
        }
    }

    return result
}

fun Table<UUID, ClaimPermission, Boolean>.toContainer(): DataContainer {
    val result: DataContainer = DataContainer.createNew()

    for (role: UUID in this.rowKeySet()) {
        val container: DataContainer = DataContainer.createNew()

        for ((permission: ClaimPermission, value: Boolean) in this.row(role)) {
            container.set(DataQuery.of(permission.toString()), value)
        }

        result.set(DataQuery.of(role.toString()), container)
    }

    return result
}
