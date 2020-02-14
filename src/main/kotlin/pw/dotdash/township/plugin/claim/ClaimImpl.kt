package pw.dotdash.township.plugin.claim

import pw.dotdash.township.api.claim.Claim
import pw.dotdash.township.api.town.Town
import pw.dotdash.township.plugin.storage.DataQueries
import com.flowpowered.math.vector.Vector3i
import com.google.common.collect.Table
import com.google.common.collect.Tables
import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.data.DataView
import org.spongepowered.api.data.Queries
import org.spongepowered.api.data.persistence.AbstractDataBuilder
import org.spongepowered.api.util.Tristate
import org.spongepowered.api.world.World
import pw.dotdash.township.api.permission.ClaimPermission
import pw.dotdash.township.api.role.Role
import pw.dotdash.township.plugin.util.*
import java.util.*

data class ClaimImpl(
    private val worldUniqueId: UUID,
    private val chunkPosition: Vector3i,
    private val townUniqueId: UUID
) : Claim {

    private val rolePermissionOverrides: Table<UUID, ClaimPermission, Boolean> =
        Tables.newCustomTable(HashMap()) { HashMap<ClaimPermission, Boolean>() }

    internal fun loadRolePermissionOverrides(table: Table<UUID, ClaimPermission, Boolean>) {
        this.rolePermissionOverrides.clear()
        this.rolePermissionOverrides.putAll(table)
    }

    override fun getWorldUniqueId(): UUID = this.worldUniqueId

    override fun getChunkPosition(): Vector3i = this.chunkPosition

    override fun getTownUniqueId(): UUID = this.townUniqueId

    override fun getPermissionOverrides(role: Role): Map<ClaimPermission, Boolean> =
        this.rolePermissionOverrides.row(role.uniqueId).toMap()

    override fun getPermissionOverride(role: Role, permission: ClaimPermission): Tristate =
        this.rolePermissionOverrides[role.uniqueId, permission].toTristate()

    override fun setPermissionOverride(role: Role, permission: ClaimPermission, value: Boolean): Boolean {
        // TODO: post event

        this.rolePermissionOverrides[role.uniqueId, permission] = value
        return true
    }

    override fun removePermissionOverride(role: Role, permission: ClaimPermission): Boolean {
        if (!this.rolePermissionOverrides.contains(role.uniqueId, permission)) return false

        // TODO: post event

        this.rolePermissionOverrides.remove(role.uniqueId, permission)
        return true
    }

    override fun getContentVersion(): Int = 1

    override fun toContainer(): DataContainer {
        return DataContainer.createNew()
            .set(Queries.CONTENT_VERSION, this.contentVersion)
            .set(DataQueries.WORLD_UNIQUE_ID, this.worldUniqueId)
            .set(DataQueries.CHUNK_POSITION, this.chunkPosition)
            .set(DataQueries.TOWN_UNIQUE_ID, this.townUniqueId)
            .set(DataQueries.ROLE_PERMISSION_OVERRIDES, this.rolePermissionOverrides.toContainer())
    }

    object DataBuilder : AbstractDataBuilder<Claim>(Claim::class.java, 1) {
        override fun buildContent(container: DataView): Optional<Claim> {
            val worldUniqueId: UUID = container.getUUID(DataQueries.WORLD_UNIQUE_ID).get()
            val chunkPosition = container.getObject(DataQueries.CHUNK_POSITION, Vector3i::class.java).get()
            val townUniqueId: UUID = container.getUUID(DataQueries.TOWN_UNIQUE_ID).get()

            val rolePermissionOverrides: Table<UUID, ClaimPermission, Boolean> =
                container.getRolePermissionOverrides(DataQueries.ROLE_PERMISSION_OVERRIDES).get()

            val claim = ClaimImpl(
                worldUniqueId = worldUniqueId,
                chunkPosition = chunkPosition,
                townUniqueId = townUniqueId
            )

            claim.loadRolePermissionOverrides(rolePermissionOverrides)

            return Optional.of(claim)
        }
    }

    class Builder : Claim.Builder {

        private var world: World? = null
        private var chunkPosition: Vector3i? = null
        private var town: Town? = null

        private val rolePermissionOverrides: Table<UUID, ClaimPermission, Boolean> =
            Tables.newCustomTable(HashMap()) { HashMap<ClaimPermission, Boolean>() }

        override fun world(world: World): Builder {
            this.world = world
            return this
        }

        override fun chunkPosition(chunkPosition: Vector3i): Builder {
            this.chunkPosition = chunkPosition
            return this
        }

        override fun town(town: Town): Builder {
            this.town = town
            return this
        }

        override fun addPermissionOverride(role: Role, permission: ClaimPermission, value: Boolean): Builder {
            this.rolePermissionOverrides[role.uniqueId, permission] = value
            return this
        }

        override fun from(value: Claim): Builder {
            this.world = value.world
            this.chunkPosition = value.chunkPosition
            this.town = value.town
            this.rolePermissionOverrides.clear()
            return this
        }

        override fun reset(): Builder {
            this.world = null
            this.chunkPosition = null
            this.town = null
            this.rolePermissionOverrides.clear()
            return this
        }

        override fun build(): Claim {
            val claim = ClaimImpl(
                worldUniqueId = checkNotNull(this.world).uniqueId,
                chunkPosition = checkNotNull(this.chunkPosition),
                townUniqueId = checkNotNull(this.town).uniqueId
            )

            claim.loadRolePermissionOverrides(this.rolePermissionOverrides)

            return claim
        }
    }
}