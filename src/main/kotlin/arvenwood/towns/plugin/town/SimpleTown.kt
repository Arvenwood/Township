package arvenwood.towns.plugin.town

import arvenwood.towns.api.claim.Claim
import arvenwood.towns.api.claim.ClaimService
import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.town.Town
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import java.util.*

data class SimpleTown(
    override val uniqueId: UUID,
    override val name: String,
    override val owner: Resident
) : Town {

    private val residentSet = HashSet<Resident>()

    override val residents: Set<Resident> get() = this.residentSet.toSet()

    override fun addResident(resident: Resident): Boolean {
        if (resident in this.residentSet) {
            return false
        }

        this.residentSet += resident
        resident.town = this
        return true
    }

    override fun removeResident(resident: Resident): Boolean {
        if (resident !in this.residentSet) {
            return false
        }

        this.residentSet -= resident
        resident.town = null
        return true
    }

    override val claims: Collection<Claim> get() = ClaimService.get().getClaimsFor(this)

    override fun getClaimAt(location: Location<World>): Claim? =
        ClaimService.get().getClaimAt(location)?.takeIf { it.town == this }

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