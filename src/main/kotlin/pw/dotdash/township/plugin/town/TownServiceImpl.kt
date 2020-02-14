package pw.dotdash.township.plugin.town

import pw.dotdash.township.api.claim.Claim
import pw.dotdash.township.api.claim.ClaimService
import pw.dotdash.township.api.resident.Resident
import pw.dotdash.township.api.town.Town
import pw.dotdash.township.api.town.TownService
import pw.dotdash.township.plugin.event.town.CreateTownEventImpl
import pw.dotdash.township.plugin.event.town.DeleteTownEventImpl
import pw.dotdash.township.plugin.storage.DataLoader
import pw.dotdash.township.plugin.storage.StorageBackedService
import pw.dotdash.township.plugin.util.tryPost
import org.spongepowered.api.Sponge
import java.util.*

class TownServiceImpl : TownService, StorageBackedService {

    private val townsById = HashMap<UUID, Town>()
    private val townsByName = HashMap<String, Town>()

    override fun getTowns(): Collection<Town> =
        this.townsById.values.toSet()

    override fun getTown(uniqueId: UUID): Optional<Town> =
        Optional.ofNullable(this.townsById[uniqueId])

    override fun getTown(name: String): Optional<Town> =
        Optional.ofNullable(this.townsByName[name])

    override fun contains(town: Town): Boolean =
        town.uniqueId in this.townsById

    override fun register(town: Town): Boolean {
        if (town.uniqueId in this.townsById) {
            return false
        }

        Sponge.getEventManager().tryPost(CreateTownEventImpl(town, Sponge.getCauseStackManager().currentCause))
            ?: return false

        this.townsById[town.uniqueId] = town
        this.townsByName[town.name] = town

        for (resident: Resident in town.residents) {
            resident.setTown(town)
        }

        return true
    }

    override fun unregister(town: Town): Boolean {
        if (town.uniqueId !in this.townsById) {
            return false
        }

        Sponge.getEventManager().tryPost(DeleteTownEventImpl(town, Sponge.getCauseStackManager().currentCause))
            ?: return false

        this.townsById.remove(town.uniqueId)
        this.townsByName.remove(town.name)

        for (resident: Resident in town.residents) {
            town.removeResident(resident)
        }

        for (claim: Claim in town.claims) {
            ClaimService.getInstance().unregister(claim)
        }

        return true
    }

    override fun load(dataLoader: DataLoader) {
        this.townsById.clear()
        this.townsByName.clear()

        for (town: Town in dataLoader.loadTowns()) {
            this.townsById[town.uniqueId] = town
            this.townsByName[town.name] = town
        }
    }

    override fun save(dataLoader: DataLoader) {
        dataLoader.saveTowns(this.townsById.values)
    }
}