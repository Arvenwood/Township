package pw.dotdash.township.plugin.role

import com.google.common.collect.Table
import com.google.common.collect.Tables
import pw.dotdash.township.api.town.TownRole
import pw.dotdash.township.api.town.TownRoleService
import pw.dotdash.township.api.town.Town
import pw.dotdash.township.plugin.storage.DataLoader
import pw.dotdash.township.plugin.storage.StorageBackedService
import pw.dotdash.township.plugin.util.set
import java.util.*

class TownRoleServiceImpl : TownRoleService, StorageBackedService {

    private val rolesById = HashMap<UUID, TownRole>()

    private val rolesByTownAndName: Table<UUID, String, TownRole> =
        Tables.newCustomTable(HashMap()) { HashMap<String, TownRole>() }

    override fun getRoles(): Collection<TownRole> =
        this.rolesById.values.toSet()

    override fun getRole(uniqueId: UUID): Optional<TownRole> =
        Optional.ofNullable(this.rolesById[uniqueId])

    override fun getRoles(town: Town): Collection<TownRole> =
        this.rolesByTownAndName.row(town.uniqueId).values.toSet()

    override fun getRole(town: Town, name: String): Optional<TownRole> =
        Optional.ofNullable(this.rolesByTownAndName[town.uniqueId, name])

    override fun contains(role: TownRole): Boolean =
        role.uniqueId in this.rolesById

    override fun register(role: TownRole): Boolean {
        if (role.uniqueId in this.rolesById) return false
        if (this.rolesByTownAndName.contains(role.townId, role.name)) return false

        // TODO: post event

        this.rolesById[role.uniqueId] = role
        this.rolesByTownAndName[role.townId, role.name] = role
        return true
    }

    override fun unregister(role: TownRole): Boolean {
        if (role.uniqueId !in this.rolesById) return false

        // TODO: post event

        this.rolesById.remove(role.uniqueId)
        this.rolesByTownAndName.remove(role.townId, role.name)
        return true
    }

    override fun load(dataLoader: DataLoader) {
        this.rolesById.clear()
        this.rolesByTownAndName.clear()

        for (role: TownRole in dataLoader.loadTownRoles()) {
            this.rolesById[role.uniqueId] = role
            this.rolesByTownAndName[role.townId, role.name] = role
        }
    }

    override fun save(dataLoader: DataLoader) {
        dataLoader.saveTownRoles(this.rolesById.values)
    }
}