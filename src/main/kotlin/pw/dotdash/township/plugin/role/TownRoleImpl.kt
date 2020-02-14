package pw.dotdash.township.plugin.role

import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.data.DataView
import org.spongepowered.api.data.Queries
import org.spongepowered.api.data.persistence.AbstractDataBuilder
import pw.dotdash.township.api.permission.Permission
import pw.dotdash.township.api.role.TownRole
import pw.dotdash.township.api.role.TownRoleService
import pw.dotdash.township.api.town.Town
import pw.dotdash.township.plugin.storage.DataQueries
import pw.dotdash.township.plugin.util.getUUID
import java.util.*
import kotlin.collections.HashSet

data class TownRoleImpl(
    private val uniqueId: UUID,
    private var name: String,
    private val townUniqueId: UUID
) : TownRole {

    private val permissions = HashSet<Permission>()

    internal fun loadPermissions(permissions: Iterable<Permission>) {
        this.permissions.clear()
        this.permissions += permissions
    }

    override fun getUniqueId(): UUID = this.uniqueId

    override fun getName(): String = this.name

    override fun setName(name: String): Boolean {
        if (this.name == name) return false
        if (TownRoleService.getInstance().getRole(this.town, name).isPresent) return false

        // TODO: post event

        this.name = name
        return true
    }

    override fun getTownUniqueId(): UUID = this.townUniqueId

    override fun getPermissions(): Collection<Permission> =
        this.permissions.toSet()

    override fun hasPermission(permission: Permission): Boolean =
        RoleType.TOWN.isValid(permission) && permission in this.permissions

    override fun addPermission(permission: Permission): Boolean {
        if (!RoleType.TOWN.isValid(permission)) return false
        if (permission in this.permissions) return false

        // TODO: post event

        this.permissions += permission
        return true
    }

    override fun removePermission(permission: Permission): Boolean {
        if (!RoleType.TOWN.isValid(permission)) return false
        if (permission !in this.permissions) return false

        // TODO: post event

        this.permissions -= permission
        return true
    }

    override fun getContentVersion(): Int = 1

    override fun toContainer(): DataContainer =
        DataContainer.createNew()
            .set(Queries.CONTENT_VERSION, this.contentVersion)
            .set(DataQueries.UNIQUE_ID, this.uniqueId)
            .set(DataQueries.NAME, this.name)
            .set(DataQueries.TOWN_UNIQUE_ID, this.townUniqueId)
            .set(DataQueries.PERMISSIONS, this.permissions)

    object DataBuilder : AbstractDataBuilder<TownRole>(TownRole::class.java, 1) {
        override fun buildContent(container: DataView): Optional<TownRole> {
            val uniqueId: UUID = container.getUUID(DataQueries.UNIQUE_ID).get()
            val name: String = container.getString(DataQueries.NAME).get()
            val townUniqueId: UUID = container.getUUID(DataQueries.TOWN_UNIQUE_ID).get()
            val permissions: List<Permission> = container.getCatalogTypeList(DataQueries.PERMISSIONS, Permission::class.java).get()

            val role = TownRoleImpl(
                uniqueId = uniqueId,
                name = name,
                townUniqueId = townUniqueId
            )

            role.loadPermissions(permissions)

            return Optional.of(role)
        }
    }

    class Builder : TownRole.Builder {

        private var name: String? = null
        private var townUniqueId: UUID? = null
        private var permissions: Set<Permission>? = null

        override fun name(name: String): TownRole.Builder {
            this.name = name
            return this
        }

        override fun town(town: Town): TownRole.Builder {
            this.townUniqueId = town.uniqueId
            return this
        }

        override fun permissions(permissions: Iterable<Permission>): TownRole.Builder {
            this.permissions = permissions.toSet()
            return this
        }

        override fun permissions(vararg permissions: Permission): TownRole.Builder {
            this.permissions = permissions.toSet()
            return this
        }

        override fun from(value: TownRole): TownRole.Builder {
            this.name = value.name
            this.townUniqueId = value.townUniqueId
            this.permissions = value.permissions.toSet()
            return this
        }

        override fun reset(): TownRole.Builder {
            this.name = null
            this.townUniqueId = null
            this.permissions = null
            return this
        }

        override fun build(): TownRole {
            val role = TownRoleImpl(
                uniqueId = UUID.randomUUID(),
                name = checkNotNull(this.name),
                townUniqueId = checkNotNull(this.townUniqueId)
            )

            this.permissions?.let(role::loadPermissions)

            return role
        }
    }
}