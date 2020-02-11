package arvenwood.towns.api.resident

import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player
import java.util.*

interface ResidentService {

    companion object {
        @JvmStatic
        fun get(): ResidentService =
            Sponge.getServiceManager().provideUnchecked(ResidentService::class.java)
    }

    val residents: Collection<Resident>

    fun getResident(uniqueId: UUID): Resident?

    fun getResident(name: String): Resident?

    fun getOrCreateResident(player: Player): Resident
}