package arvenwood.towns.plugin.town

import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.town.Town
import arvenwood.towns.plugin.storage.DataQueries
import arvenwood.towns.plugin.util.getResidentByUUID
import arvenwood.towns.plugin.util.getResidentsByUUIDList
import arvenwood.towns.plugin.util.getUUID
import org.spongepowered.api.data.DataView
import org.spongepowered.api.data.persistence.AbstractDataBuilder
import org.spongepowered.api.data.persistence.DataBuilder
import java.util.*

class TownDataBuilder : AbstractDataBuilder<Town>(Town::class.java, 1) {

    override fun buildContent(container: DataView): Optional<Town> {
        val uniqueId: UUID = container.getUUID(DataQueries.UNIQUE_ID).get()
        val name: String = container.getString(DataQueries.NAME).get()
        val open: Boolean = container.getBoolean(DataQueries.OPEN).get()
        val owner: Resident = container.getResidentByUUID(DataQueries.OWNER_UNIQUE_ID).get()
        val residents: List<Resident> = container.getResidentsByUUIDList(DataQueries.RESIDENT_UNIQUE_IDS).get()

        val town = TownImpl(
            uniqueId = uniqueId,
            name = name,
            open = open,
            owner = owner
        )

        residents.forEach(town::loadResident)

        return Optional.of(town)
    }
}