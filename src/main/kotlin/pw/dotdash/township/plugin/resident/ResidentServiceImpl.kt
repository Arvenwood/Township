package pw.dotdash.township.plugin.resident

import pw.dotdash.township.api.resident.Resident
import pw.dotdash.township.api.resident.ResidentService
import pw.dotdash.township.plugin.storage.DataLoader
import pw.dotdash.township.plugin.storage.StorageBackedService
import org.spongepowered.api.entity.living.player.Player
import pw.dotdash.township.plugin.util.unwrap
import java.util.*
import kotlin.collections.HashMap

class ResidentServiceImpl : ResidentService, StorageBackedService {

    private val residentsById = HashMap<UUID, Resident>()
    private val residentsByName = HashMap<String, Resident>()

    private val systemResident = ResidentImpl(
        uniqueId = UUID(0, 0),
        name = "T-BOT",
        townUniqueId = null
    )

    override fun getSystemResident(): Resident =
        this.systemResident

    override fun getResidents(): Collection<Resident> =
        this.residentsById.values.toSet()

    override fun getResident(uniqueId: UUID): Optional<Resident> =
        Optional.ofNullable(this.residentsById[uniqueId])

    override fun getResident(name: String): Optional<Resident> =
        Optional.ofNullable(this.residentsByName[name])

    override fun getOrCreateResident(player: Player): Resident {
        this.getResident(player.uniqueId).unwrap()?.let { return it }

        val resident = ResidentImpl(player)

        this.residentsById[resident.uniqueId] = resident
        this.residentsByName[resident.name] = resident

        return resident
    }

    override fun load(dataLoader: DataLoader) {
        this.residentsById.clear()
        this.residentsByName.clear()

        for (resident: Resident in dataLoader.loadResidents()) {
            this.residentsById[resident.uniqueId] = resident
            this.residentsByName[resident.name] = resident
        }
    }

    override fun save(dataLoader: DataLoader) {
        dataLoader.saveResidents(this.residentsById.values)
    }
}