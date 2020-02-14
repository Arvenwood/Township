package pw.dotdash.township.plugin.town

import pw.dotdash.township.api.resident.Resident
import pw.dotdash.township.api.town.Town
import pw.dotdash.township.plugin.storage.DataQueries
import pw.dotdash.township.plugin.util.getResidentByUUID
import pw.dotdash.township.plugin.util.getResidentsByUUIDList
import pw.dotdash.township.plugin.util.getUUID
import org.spongepowered.api.data.DataView
import org.spongepowered.api.data.persistence.AbstractDataBuilder
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