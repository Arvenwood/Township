package arvenwood.towns.plugin.town

import arvenwood.towns.api.claim.Claim
import arvenwood.towns.api.claim.ClaimService
import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.town.Town
import arvenwood.towns.plugin.event.town.ChangeTownEventImpl
import org.spongepowered.api.Sponge
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import java.util.*

data class SimpleTown(
    private val uniqueId: UUID,
    private var name: String,
    private var owner: Resident
) : Town {

    private val residents = HashSet<Resident>()

    override fun getUniqueId(): UUID =
        this.uniqueId

    override fun getName(): String =
        this.name

    override fun setName(name: String) {
        val event = ChangeTownEventImpl.Name(this.name, name, this, Sponge.getCauseStackManager().currentCause)
        Sponge.getEventManager().post(event)
        if (event.isCancelled) {
            return
        }

        this.name = event.newName
    }

    override fun getOwner(): Resident =
        this.owner

    override fun setOwner(resident: Resident) {
        val event = ChangeTownEventImpl.Owner(this.owner, resident, this, Sponge.getCauseStackManager().currentCause)
        Sponge.getEventManager().post(event)
        if (event.isCancelled) {
            return
        }

        this.owner = event.newOwner
    }

    override fun getResidents(): Collection<Resident> =
        this.residents.toSet()

    override fun addResident(resident: Resident): Boolean {
        if (resident in this.residents) {
            return false
        }

        val event = ChangeTownEventImpl.Join(resident, this, Sponge.getCauseStackManager().currentCause)
        Sponge.getEventManager().post(event)
        if (event.isCancelled) {
            return false
        }

        this.residents += resident
        resident.setTown(this)
        return true
    }

    override fun removeResident(resident: Resident): Boolean {
        if (resident !in this.residents) {
            return false
        }

        val event = ChangeTownEventImpl.Leave(resident, this, Sponge.getCauseStackManager().currentCause)
        Sponge.getEventManager().post(event)
        if (event.isCancelled) {
            return false
        }

        this.residents -= resident
        resident.setTown(null)
        return true
    }

    override fun getClaimAt(location: Location<World>): Optional<Claim> =
        ClaimService.get().getClaimAt(location).filter { it.town == this }

    class Builder : Town.Builder {

        private var name: String? = null
        private var owner: Resident? = null
        private var residents: Set<Resident>? = null

        override fun name(name: String): Town.Builder {
            this.name = name
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
            this.owner = value.owner
            this.residents = value.residents.toSet()
            return this
        }

        override fun reset(): Town.Builder {
            this.name = null
            this.owner = null
            this.residents = null
            return this
        }

        override fun build(): Town {
            val residents: Set<Resident>? = this.residents
            val owner: Resident = checkNotNull(this.owner)

            val town: Town = SimpleTown(
                UUID.randomUUID(), checkNotNull(this.name),
                owner
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