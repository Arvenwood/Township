package pw.dotdash.township.plugin.town

import pw.dotdash.township.api.event.town.ChangeTownEvent
import pw.dotdash.township.api.resident.Resident
import pw.dotdash.township.api.town.Town
import pw.dotdash.township.plugin.event.town.ChangeTownEventImpl
import pw.dotdash.township.plugin.storage.DataQueries
import org.spongepowered.api.Sponge
import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.data.DataView
import org.spongepowered.api.data.persistence.AbstractDataBuilder
import org.spongepowered.api.service.economy.Currency
import org.spongepowered.api.service.economy.EconomyService
import org.spongepowered.api.service.economy.account.Account
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.channel.MessageChannel
import pw.dotdash.township.api.nation.Nation
import pw.dotdash.township.api.permission.ClaimPermission
import pw.dotdash.township.api.resident.ResidentService
import pw.dotdash.township.api.town.TownRole
import pw.dotdash.township.api.town.TownService
import pw.dotdash.township.plugin.role.TownRoleImpl
import pw.dotdash.township.plugin.util.*
import java.math.BigDecimal
import java.util.*

data class TownImpl(
    private val uniqueId: UUID,
    private var name: String,
    private var open: Boolean,
    private var ownerId: UUID,
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

    override fun setOpen(open: Boolean): Boolean {
        if (this.open == open) return false

        Sponge.getEventManager()
            .tryPost(ChangeTownEventImpl.Open(open, this, Sponge.getCauseStackManager().currentCause))
            ?: return false

        this.open = open
        return true
    }

    override fun getOwner(): Resident =
        ResidentService.getInstance().getResident(this.ownerId)
            .orElseThrow { IllegalStateException("Resident $ownerId is not loaded") }

    override fun isOwner(resident: Resident): Boolean =
        this.ownerId == resident.uniqueId

    override fun setOwner(resident: Resident): Boolean {
        if (this.isOwner(resident)) return false

        val event: ChangeTownEvent.Owner = Sponge.getEventManager()
            .tryPost(ChangeTownEventImpl.Owner(this.owner, resident, this, Sponge.getCauseStackManager().currentCause))
            ?: return false

        this.ownerId = event.newOwner.uniqueId
        return true
    }

    override fun getNation(): Optional<Nation> {
        TODO()
    }

    override fun hasNation(): Boolean {
        TODO()
    }

    override fun setNation(nation: Nation?): Boolean {
        TODO()
    }

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

    override fun getVisitorRole(): TownRole = this.visitorRole

    override fun getAccount(): Optional<Account> =
        Sponge.getServiceManager().provide(EconomyService::class.java)
            .flatMap { it.getOrCreateAccount("town-$uniqueId") }

    override fun getBalance(currency: Currency): BigDecimal =
        this.account
            .map { it.getBalance(currency) }
            .orElse(BigDecimal.ZERO)

    override fun getBalance(): BigDecimal =
        this.account
            .flatMap { acc: Account ->
                Sponge.getServiceManager().provide(EconomyService::class.java)
                    .map { acc.getBalance(it.defaultCurrency) }
            }
            .orElse(BigDecimal.ZERO)

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
            .set(DataQueries.OWNER_ID, this.ownerId)
            .set(DataQueries.VISITOR_ROLE, this.visitorRole)
            .set(DataQueries.RESIDENT_UNIQUE_IDS, this.residents)

    object DataBuilder : AbstractDataBuilder<Town>(Town::class.java, 1) {
        override fun buildContent(container: DataView): Optional<Town> {
            val uniqueId: UUID = container.getUUID(DataQueries.UNIQUE_ID).get()
            val name: String = container.getString(DataQueries.NAME).get()
            val open: Boolean = container.getBoolean(DataQueries.OPEN).get()
            val ownerId: UUID = container.getUUID(DataQueries.OWNER_ID).get()
            val visitorRole: TownRole = container.getSerializable(DataQueries.VISITOR_ROLE, TownRole::class.java).get()
            val residents: List<UUID> = container.getUUIDList(DataQueries.RESIDENT_UNIQUE_IDS).get()

            val town = TownImpl(
                uniqueId = uniqueId,
                name = name,
                open = open,
                ownerId = ownerId,
                visitorRole = visitorRole
            )

            town.loadResidents(residents)

            return Optional.of(town)
        }
    }

    class Builder : Town.Builder {

        private var name: String? = null
        private var open: Boolean = false
        private var ownerId: UUID? = null
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
            this.ownerId = owner.uniqueId
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
            this.ownerId = value.owner.uniqueId
            this.residents = value.residents.mapTo(HashSet(), Resident::getUniqueId)

            this.visitorRolePermissions.clear()
            this.visitorRolePermissions += value.visitorRole.permissions.filterIsInstance<ClaimPermission>()
            return this
        }

        override fun reset(): Town.Builder {
            this.name = null
            this.open = false
            this.ownerId = null
            this.residents = null
            this.visitorRolePermissions.clear()
            return this
        }

        override fun build(): Town {
            val ownerId: UUID = checkNotNull(this.ownerId)

            val uniqueId: UUID = UUID.randomUUID()

            val visitorRole = TownRoleImpl(
                uniqueId = UUID(0, 0),
                name = "visitor",
                priority = 0,
                townId = uniqueId
            )
            visitorRole.loadPermissions(this.visitorRolePermissions)

            val town = TownImpl(
                uniqueId = uniqueId,
                name = checkNotNull(this.name),
                open = this.open,
                ownerId = ownerId,
                visitorRole = visitorRole
            )

            this.residents?.let(town::loadResidents)
            town.loadResidents(listOf(ownerId))

            return town
        }
    }
}