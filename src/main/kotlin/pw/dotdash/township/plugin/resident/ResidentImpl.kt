package pw.dotdash.township.plugin.resident

import pw.dotdash.township.api.resident.Resident
import pw.dotdash.township.api.town.Town
import pw.dotdash.township.plugin.storage.DataQueries
import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.data.DataView
import org.spongepowered.api.data.Queries
import org.spongepowered.api.data.persistence.AbstractDataBuilder
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.channel.MessageChannel
import org.spongepowered.api.util.Tristate
import pw.dotdash.township.api.claim.Claim
import pw.dotdash.township.api.permission.ClaimPermission
import pw.dotdash.township.api.permission.Permission
import pw.dotdash.township.api.permission.TownPermission
import pw.dotdash.township.api.resident.ResidentService
import pw.dotdash.township.api.permission.Role
import pw.dotdash.township.api.town.TownRole
import pw.dotdash.township.api.town.TownRoleService
import pw.dotdash.township.api.town.TownService
import pw.dotdash.township.plugin.util.getUUID
import pw.dotdash.township.plugin.util.getUUIDList
import pw.dotdash.township.plugin.util.unwrap
import java.util.*

data class ResidentImpl(
    private val uniqueId: UUID,
    private val name: String,
    private var townUniqueId: UUID?
) : Resident {
    constructor(player: Player) : this(player.uniqueId, player.name, null)

    private val friends: MutableSet<UUID> = HashSet()
    private val townRoles: MutableSet<UUID> = HashSet()

    internal fun loadFriends(friends: Iterable<UUID>) {
        this.friends.clear()
        this.friends += friends
    }

    internal fun loadTownRoles(townRoles: Iterable<UUID>) {
        this.townRoles.clear()
        this.townRoles += townRoles
    }

    override fun getUniqueId(): UUID = this.uniqueId

    override fun getName(): String = this.name

    override fun getTown(): Optional<Town> =
        Optional.ofNullable(this.townUniqueId).flatMap { TownService.getInstance().getTown(it) }

    override fun hasTown(): Boolean =
        this.townUniqueId != null

    override fun setTown(town: Town?): Boolean {
        getTown().ifPresent {
            it.removeResident(this)
        }

        this.townUniqueId = town?.uniqueId
        this.townRoles.clear()
        return town?.addResident(this) == true
    }

    override fun getFriends(): Collection<Resident> =
        this.friends.mapNotNull { ResidentService.getInstance().getResident(it).unwrap() }

    override fun hasFriend(resident: Resident): Boolean {
        TODO()
    }

    override fun addFriend(resident: Resident): Boolean {
        TODO()
    }

    override fun removeFriend(resident: Resident): Boolean {
        TODO()
    }

    override fun getTownRoles(): Collection<TownRole> {
        val roles = ArrayList<TownRole>()
        this.town.ifPresent { roles += it.visitorRole }
        this.townRoles.mapNotNullTo(roles) { TownRoleService.getInstance().getRole(it).unwrap() }
        roles.sortByDescending(TownRole::getPriority)
        return roles
    }

    override fun hasRole(role: TownRole): Boolean =
        role.uniqueId in this.townRoles

    override fun addRole(role: TownRole): Boolean {
        if (role.uniqueId in this.townRoles) return false
        if (this.townUniqueId != role.town.uniqueId) return false

        // TODO: post event

        this.townRoles += role.uniqueId
        return true
    }

    override fun removeRole(role: TownRole): Boolean {
        if (role.uniqueId !in this.townRoles) return false

        // TODO: post event

        this.townRoles -= role.uniqueId
        return true
    }

    private fun getContextualRoles(claim: Claim): Collection<TownRole> =
        if (claim.town.uniqueId == this.townUniqueId) {
            // In their own town?
            this.getTownRoles()
        } else {
            // Not in their own town: use visitor role.
            listOf(claim.town.visitorRole)
        }

    override fun getPermissionValue(permission: Permission, claim: Claim): Tristate {
        val roles: Collection<Role> = this.getContextualRoles(claim)

        when (permission) {
            is TownPermission, is ClaimPermission  -> {
                if (claim.town.isOwner(this)) {
                    // Town owner can always do stuff in their town.
                    return Tristate.TRUE
                }

                // Check for any roles with the permission.
                for (role: Role in roles) {
                    val value: Tristate = role.getPermissionValue(permission, claim)
                    if (value != Tristate.UNDEFINED) {
                        return value
                    }
                }

                return Tristate.UNDEFINED
            }
            else -> {
                // Unsupported permission type.
                return Tristate.UNDEFINED
            }
        }
    }

    override fun sendMessage(message: Text) {
        this.player.ifPresent { player: Player ->
            player.sendMessage(message)
        }
    }

    override fun setMessageChannel(channel: MessageChannel) {
        this.player.ifPresent { player: Player ->
            player.messageChannel = channel
        }
    }

    override fun getMessageChannel(): MessageChannel =
        this.player.map(Player::getMessageChannel).orElse(MessageChannel.TO_NONE)

    override fun getContentVersion(): Int = 1

    override fun toContainer(): DataContainer {
        val container: DataContainer = DataContainer.createNew()
            .set(Queries.CONTENT_VERSION, this.contentVersion)
            .set(DataQueries.UNIQUE_ID, this.uniqueId)
            .set(DataQueries.NAME, this.name)
            .set(DataQueries.FRIENDS, this.friends)
            .set(DataQueries.TOWN_ROLES, this.townRoles)

        this.townUniqueId?.let { container.set(DataQueries.TOWN_ID, it) }

        return container
    }

    object DataBuilder : AbstractDataBuilder<Resident>(Resident::class.java, 1) {
        override fun buildContent(container: DataView): Optional<Resident> {
            val uniqueId: UUID = container.getUUID(DataQueries.UNIQUE_ID).get()
            val name: String = container.getString(DataQueries.NAME).get()
            val townUniqueId: UUID? = container.getUUID(DataQueries.TOWN_ID).unwrap()

            val friends: List<UUID> = container.getUUIDList(DataQueries.FRIENDS).get()
            val townRoles: List<UUID> = container.getUUIDList(DataQueries.TOWN_ROLES).get()

            val resident = ResidentImpl(
                uniqueId = uniqueId,
                name = name,
                townUniqueId = townUniqueId
            )

            resident.loadFriends(friends)
            resident.loadTownRoles(townRoles)

            return Optional.of(resident)
        }
    }
}