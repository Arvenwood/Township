package arvenwood.towns.plugin.town

import arvenwood.towns.api.claim.Claim
import arvenwood.towns.api.claim.ClaimService
import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.town.Town
import arvenwood.towns.api.town.TownService
import arvenwood.towns.plugin.event.town.CreateTownEventImpl
import arvenwood.towns.plugin.event.town.DeleteTownEventImpl
import arvenwood.towns.plugin.storage.DataLoader
import arvenwood.towns.plugin.storage.StorageBackedService
import arvenwood.towns.plugin.util.tryPost
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