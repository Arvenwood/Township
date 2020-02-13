package arvenwood.towns.plugin.town

import arvenwood.towns.api.event.town.ChangeTownEvent
import arvenwood.towns.api.invite.Invite
import arvenwood.towns.api.invite.InviteService
import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.town.Town
import arvenwood.towns.plugin.event.town.ChangeTownEventImpl
import arvenwood.towns.plugin.storage.DataQueries
import arvenwood.towns.plugin.util.tryPost
import org.spongepowered.api.Sponge
import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.service.economy.EconomyService
import org.spongepowered.api.service.economy.account.Account
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.channel.MessageChannel
import java.util.*

data class TownImpl(
    private val uniqueId: UUID,
    private var name: String,
    private var open: Boolean,
    private var owner: Resident
) : Town {

    private var messageChannel: MessageChannel = TownMessageChannel(this)

    private val residents = HashSet<Resident>()

    internal fun loadResident(resident: Resident) {
        this.residents += resident
    }

    override fun getUniqueId(): UUID =
        this.uniqueId

    override fun getName(): String =
        this.name

    override fun setName(name: String) {
        if (this.name == name) return

        val event: ChangeTownEvent.Name = Sponge.getEventManager()
            .tryPost(ChangeTownEventImpl.Name(this.name, name, this, Sponge.getCauseStackManager().currentCause))
            ?: return

        this.name = event.newName
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

    override fun getResidents(): Collection<Resident> =
        this.residents.toSet()

    override fun addResident(resident: Resident): Boolean {
        if (resident in this.residents) {
            return false
        }

        Sponge.getEventManager()
            .tryPost(ChangeTownEventImpl.Join(resident, this, Sponge.getCauseStackManager().currentCause))
            ?: return false

        this.residents += resident
        resident.setTown(this)
        return true
    }

    override fun removeResident(resident: Resident): Boolean {
        if (resident !in this.residents) {
            return false
        }

        Sponge.getEventManager()
            .tryPost(ChangeTownEventImpl.Leave(resident, this, Sponge.getCauseStackManager().currentCause))
            ?: return false

        this.residents -= resident
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
            .set(DataQueries.UNIQUE_ID, this.uniqueId.toString())
            .set(DataQueries.NAME, this.name)
            .set(DataQueries.OPEN, this.open)
            .set(DataQueries.OWNER_UNIQUE_ID, this.owner.uniqueId.toString())
            .set(DataQueries.RESIDENT_UNIQUE_IDS, this.residents.map { it.uniqueId.toString() })

    class Builder : Town.Builder {

        private var name: String? = null
        private var open: Boolean = false
        private var owner: Resident? = null
        private var residents: Set<Resident>? = null

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
            this.residents = residents.toSet()
            return this
        }

        override fun residents(vararg residents: Resident): Town.Builder {
            this.residents = residents.toSet()
            return this
        }

        override fun from(value: Town): Town.Builder {
            this.name = value.name
            this.open = value.isOpen
            this.owner = value.owner
            this.residents = value.residents.toSet()
            return this
        }

        override fun reset(): Town.Builder {
            this.name = null
            this.open = false
            this.owner = null
            this.residents = null
            return this
        }

        override fun build(): Town {
            val residents: Set<Resident>? = this.residents
            val owner: Resident = checkNotNull(this.owner)

            val town: Town = TownImpl(
                uniqueId = UUID.randomUUID(),
                name = checkNotNull(this.name),
                open = this.open,
                owner = owner
            )

            if (residents != null) {
                for (resident in residents) {
                    town.addResident(resident)
                }
            }
            town.addResident(owner)

            return town
        }
    }
}