package arvenwood.towns.api.town

import org.spongepowered.api.Sponge
import java.util.*

interface TownService {

    companion object {
        @JvmStatic
        fun get(): TownService =
            Sponge.getServiceManager().provideUnchecked(TownService::class.java)
    }

    val towns: Collection<Town>

    fun getTown(uniqueId: UUID): Town?

    fun getTown(name: String): Town?

    fun register(town: Town): Boolean

    fun unregister(town: Town): Boolean
}