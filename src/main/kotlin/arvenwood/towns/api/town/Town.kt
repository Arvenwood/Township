package arvenwood.towns.api.town

import arvenwood.towns.api.claim.Claim
import arvenwood.towns.api.resident.Resident
import org.spongepowered.api.Sponge
import org.spongepowered.api.util.ResettableBuilder
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import java.util.*

interface Town {

    companion object {
        @JvmStatic
        fun builder(): Builder =
            Sponge.getRegistry().createBuilder(Builder::class.java)
    }

    val uniqueId: UUID

    val name: String

    val owner: Resident

    val residents: Collection<Resident>

    fun addResident(resident: Resident): Boolean

    fun removeResident(resident: Resident): Boolean

    val claims: Collection<Claim>

    fun getClaimAt(location: Location<World>): Claim?

    interface Builder : ResettableBuilder<Town, Builder> {

        fun name(name: String): Builder

        fun owner(owner: Resident): Builder

        fun residents(residents: Iterable<Resident>): Builder

        fun residents(vararg residents: Resident): Builder =
            residents(residents.toList())

        fun build(): Town
    }
}