package arvenwood.towns.plugin.resident

import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.resident.ResidentService
import org.spongepowered.api.entity.living.player.Player
import java.util.*
import kotlin.collections.HashMap

class SimpleResidentService : ResidentService {

    private val residentsById = HashMap<UUID, Resident>()
    private val residentsByName = HashMap<String, Resident>()

    override val residents: Collection<Resident>
        get() = this.residentsById.values

    override fun getResident(uniqueId: UUID): Resident? =
        this.residentsById[uniqueId]

    override fun getResident(name: String): Resident? =
        this.residentsByName[name]

    override fun getOrCreateResident(player: Player): Resident {
        this.getResident(player.uniqueId)?.let { return it }

        val resident = SimpleResident(
            uniqueId = player.uniqueId,
            name = player.name,
            town = null
        )

        this.residentsById[resident.uniqueId] = resident
        this.residentsByName[resident.name] = resident

        return resident
    }
}