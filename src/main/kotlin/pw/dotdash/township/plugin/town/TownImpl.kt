package pw.dotdash.township.plugin.town

import pw.dotdash.township.api.event.town.ChangeTownEvent
import pw.dotdash.township.api.invite.Invite
import pw.dotdash.township.api.invite.InviteService
import pw.dotdash.township.api.resident.Resident
import pw.dotdash.township.api.town.Town
import pw.dotdash.township.plugin.event.town.ChangeTownEventImpl
import pw.dotdash.township.plugin.storage.DataQueries
import org.spongepowered.api.Sponge
import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.data.DataView
import org.spongepowered.api.data.persistence.AbstractDataBuilder
import org.spongepowered.api.service.economy.EconomyService
import org.spongepowered.api.service.economy.account.Account
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.channel.MessageChannel
import pw.dotdash.township.api.permission.ClaimPermission
import pw.dotdash.township.api.permission.Permission
import pw.dotdash.township.api.resident.ResidentService
import pw.dotdash.township.api.role.TownRole
import pw.dotdash.township.api.town.TownService
import pw.dotdash.township.plugin.role.TownRoleImpl
import pw.dotdash.township.plugin.util.*
import java.util.*

data class TownImpl(
    private val uniqueId: UUID,
    private var name: String,
    private var open: Boolean,
    private var owner: Resident,
    private val visitorRole: TownRole
) : Town {

    private var messageChannel: MessageChannel = TownMessageChannel(this)

    private val residents = HashSet<UUID>()

    internal fun loadResidents(residents: Iterable<UUID>) {
        this.residents.clear()
        this.residents += residents
    }

    override fun getUniqueId(): UUID =
        this.uniqueId

    override fun getName(): String =
        this.name

    override fun setName(name: String): Boolean {
        if (this.name == name) return false
        if (TownService.getInstance().getTown(name).isPresent) return false

        val event: ChangeTownEvent.Name = Sponge.getEventManager()
            .tryPost(ChangeTownEventImpl.Name(this.name, name, this, Sponge.getCauseStackManager().currentCause))
            ?: return false

        this.name = event.newName
        return true
    }

    override fun isOpen(): Boolean = this.open

    override fun setOpen(open: Boolean) {
        if (this.open == open) return

        Sponge.getEventManager()
            .tryPost(ChangeTownEventImpl.Open(open, this, Sponge.getCauseStackManager().currentCause))
            ?: return

        this.open = open
    }

    override fun getOwner(): Resident =
        this.owner

    override fun setOwner(resident: Resident) {
        if (this.owner == resident) return

        val event: ChangeTownEvent.Owner = Sponge.getEventManager()
            .tryPost(ChangeTownEventImpl.Owner(this.owner, resident, this, Sponge.getCauseStackManager().currentCause))
            ?: return

        this.owner = event.newOwner
    }

    override fun getResidentIds(): Collection<UUID> =
        this.residents.toSet()

    override fun getResidents(): Collection<Resident> =
        this.residents.mapNotNull { ResidentService.getInstance().getResident(it).unwrap() }

    override fun hasResident(resident: Resident): Boolean =
        resident.uniqueId in this.residents

    override fun addResident(resident: Resident): Boolean {
        if (resident.uniqueId in this.residents) return false

        Sponge.getEventManager()
            .tryPost(ChangeTownEventImpl.Join(resident, this, Sponge.getCauseStackManager().currentCause))
            ?: return false

        this.residents += resident.uniqueId
        resident.setTown(this)
        return true
    }

    override fun removeResident(resident: Resident): Boolean {
        if (resident.uniqueId !in this.residents) return false

        Sponge.getEventManager()
            .tryPost(ChangeTownEventImpl.Leave(resident, this, Sponge.getCauseStackManager().currentCause))
            ?: return false

        this.residents -= resident.uniqueId
        resident.setTown(null)
        return true
    }

    override fun inviteResident(sender: Resident, receiver: Resident): Invite {
        val invite: Invite = Invite.builder()
            .sender(sender)
            .receiver(receiver)
            .town(this)
            .build()

        InviteService.getInstance().register(invite)
        return invite
    }

    override fun getVisitorRole(): TownRole = this.visitorRole

    override fun getAccount(): Optional<Account> =
        Sponge.getServiceManager().provide(EconomyService::class.java)
            .flatMap { it.getOrCreateAccount("town-$uniqueId") }

    override fun sendMessage(message: Text) {
        this.messageChannel.send(message)
    }

    override fun setMessageChannel(channel: MessageChannel) {
        this.messageChannel = channel
    }

    override fun getMessageChannel(): MessageChannel =
        this.messageChannel

    override fun getContentVersion(): Int = 1

    override fun toContainer(): DataContainer =
        DataContainer.createNew()
            .set(DataQueries.UNIQUE_ID, this.uniqueId)
            .set(DataQueries.NAME, this.name)
            .set(DataQueries.OPEN, this.open)
            .set(DataQueries.OWNER_UNIQUE_ID, this.owner.uniqueId)
            .set(DataQueries.VISITOR_ROLE, this.visitorRole)
            .set(DataQueries.RESIDENT_UNIQUE_IDS, this.residents)

    object DataBuilder : AbstractDataBuilder<Town>(Town::class.java, 1) {
        override fun buildContent(container: DataView): Optional<Town> {
            val uniqueId: UUID = container.getUUID(DataQueries.UNIQUE_ID).get()
            val name: String = container.getString(DataQueries.NAME).get()
            val open: Boolean = container.getBoolean(DataQueries.OPEN).get()
            val owner: Resident = container.getResidentByUUID(DataQueries.OWNER_UNIQUE_ID).get()
            val visitorRole: TownRole = container.getSerializable(DataQueries.VISITOR_ROLE, TownRole::class.java).get()
            val residents: List<UUID> = container.getUUIDList(DataQueries.RESIDENT_UNIQUE_IDS).get()

            val town = TownImpl(
                uniqueId = uniqueId,
                name = name,
                open = open,
                owner = owner,
                visitorRole = visitorRole
            )

            town.loadResidents(residents)

            return Optional.of(town)
        }
    }

    class Builder : Town.Builder {

        private var name: String? = null
        private var open: Boolean = false
        private var owner: Resident? = null
        private var residents: Set<UUID>? = null

        private val visitorRolePermissions = HashSet<ClaimPermission>()

        override fun name(name: String): Town.Builder {
            this.name = name
            return this
        }

        override fun open(open: Boolean): Town.Builder {
            this.open = open
            return this
        }

        override fun owner(owner: Resident): Town.Builder {
            this.owner = owner
            return this
        }

        override fun residents(residents: Iterable<Resident>): Town.Builder {
            this.residents = residents.mapTo(HashSet(), Resident::getUniqueId)
            return this
        }

        override fun residents(vararg residents: Resident): Town.Builder {
            this.residents = residents.mapTo(HashSet(), Resident::getUniqueId)
            return this
        }

        override fun addVisitorRolePermission(permission: ClaimPermission): Town.Builder {
            this.visitorRolePermissions += permission
            return this
        }

        override fun from(value: Town): Town.Builder {
            this.name = value.name
            this.open = value.isOpen
            this.owner = value.owner
            this.residents = value.residentIds.toSet()

            this.visitorRolePermissions.clear()
            this.visitorRolePermissions += value.visitorRole.permissions.filterIsInstance<ClaimPermission>()
            return this
        }

        override fun reset(): Town.Builder {
            this.name = null
            this.open = false
            this.owner = null
            this.residents = null
            this.visitorRolePermissions.clear()
            return this
        }

        override fun build(): Town {
            val owner: Resident = checkNotNull(this.owner)

            val uniqueId: UUID = UUID.randomUUID()

            val visitorRole = TownRoleImpl(
                uniqueId = UUID(0, 0),
                name = "visitor",
                townUniqueId = uniqueId
            )
            visitorRole.loadPermissions(this.visitorRolePermissions)

            val town = TownImpl(
                uniqueId = uniqueId,
                name = checkNotNull(this.name),
                open = this.open,
                owner = owner,
                visitorRole = visitorRole
            )

            this.residents?.let(town::loadResidents)
            town.loadResidents(listOf(owner.uniqueId))

            return town
        }
    }
}