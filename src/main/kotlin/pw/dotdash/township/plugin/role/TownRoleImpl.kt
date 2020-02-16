package pw.dotdash.township.plugin.role

import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.data.DataView
import org.spongepowered.api.data.Queries
import org.spongepowered.api.data.persistence.AbstractDataBuilder
import org.spongepowered.api.util.Tristate
import pw.dotdash.township.api.claim.Claim
import pw.dotdash.township.api.permission.ClaimPermission
import pw.dotdash.township.api.permission.Permission
import pw.dotdash.township.api.permission.TownPermission
import pw.dotdash.township.api.permission.TownPermissions
import pw.dotdash.township.api.town.TownRole
import pw.dotdash.township.api.town.TownRoleService
import pw.dotdash.township.api.town.Town
import pw.dotdash.township.api.town.TownService
import pw.dotdash.township.plugin.storage.DataQueries
import pw.dotdash.township.plugin.util.getUUID
import java.util.*
import kotlin.collections.HashSet

data class TownRoleImpl(
    private val uniqueId: UUID,
    private var priority: Int,
    private var name: String,
    private val townId: UUID
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

    override fun getPriority(): Int = this.priority

    override fun setPriority(priority: Int): Boolean {
        if (this.priority == priority) return false

        // TODO: post event

        this.priority = priority
        return true
    }

    override fun getTownId(): UUID = this.townId

    override fun getTown(): Town =
        TownService.getInstance()
            .getTown(this.townId)
            .orElseThrow { IllegalStateException("Town $townId is not loaded") }

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

    override fun getPermissionValue(permission: Permission, claim: Claim): Tristate {
        when (permission) {
            is TownPermission -> {
                return Tristate.fromBoolean(permission in this.permissions || TownPermissions.TOWN_DEPUTY in this.permissions)
            }
            is ClaimPermission -> {
                // Check for DEPUTY permission first.
                if (TownPermissions.TOWN_DEPUTY in this.permissions) {
                    return Tristate.TRUE
                }

                // Then check for claim overrides.
                val override: Tristate = claim.getPermissionOverride(this, permission)
                if (override != Tristate.UNDEFINED) {
                    return override
                }

                // Then check if this role has the permission.
                return Tristate.fromBoolean(permission in this.permissions)
            }
            else -> {
                return Tristate.UNDEFINED
            }
        }
    }

    override fun getContentVersion(): Int = 1

    override fun toContainer(): DataContainer =
        DataContainer.createNew()
            .set(Queries.CONTENT_VERSION, this.contentVersion)
            .set(DataQueries.UNIQUE_ID, this.uniqueId)
            .set(DataQueries.NAME, this.name)
            .set(DataQueries.PRIORITY, this.priority)
            .set(DataQueries.TOWN_ID, this.townId)
            .set(DataQueries.PERMISSIONS, this.permissions)

    object DataBuilder : AbstractDataBuilder<TownRole>(TownRole::class.java, 1) {
        override fun buildContent(container: DataView): Optional<TownRole> {
            val uniqueId: UUID = container.getUUID(DataQueries.UNIQUE_ID).get()
            val name: String = container.getString(DataQueries.NAME).get()
            val priority: Int = container.getInt(DataQueries.PRIORITY).get()
            val townUniqueId: UUID = container.getUUID(DataQueries.TOWN_ID).get()
            val permissions: List<Permission> = container.getCatalogTypeList(DataQueries.PERMISSIONS, Permission::class.java).get()

            val role = TownRoleImpl(
                uniqueId = uniqueId,
                name = name,
                priority = priority,
                townId = townUniqueId
            )

            role.loadPermissions(permissions)

            return Optional.of(role)
        }
    }

    class Builder : TownRole.Builder {

        private var name: String? = null
        private var priority: Int = 0
        private var townUniqueId: UUID? = null
        private var permissions: Set<Permission>? = null

        override fun name(name: String): TownRole.Builder {
            this.name = name
            return this
        }

        override fun priority(priority: Int): TownRole.Builder {
            this.priority = priority
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
            this.priority = value.priority
            this.townUniqueId = value.town.uniqueId
            this.permissions = value.permissions.toSet()
            return this
        }

        override fun reset(): TownRole.Builder {
            this.name = null
            this.priority = 0
            this.townUniqueId = null
            this.permissions = null
            return this
        }

        override fun build(): TownRole {
            val role = TownRoleImpl(
                uniqueId = UUID.randomUUID(),
                name = checkNotNull(this.name),
                priority = this.priority,
                townId = checkNotNull(this.townUniqueId)
            )

            this.permissions?.let(role::loadPermissions)

            return role
        }
    }
}