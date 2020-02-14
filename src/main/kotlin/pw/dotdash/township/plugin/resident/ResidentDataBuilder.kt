package pw.dotdash.township.plugin.resident

import pw.dotdash.township.api.resident.Resident
import pw.dotdash.township.plugin.storage.DataQueries
import pw.dotdash.township.plugin.util.getUUID
import org.spongepowered.api.data.DataView
import org.spongepowered.api.data.persistence.AbstractDataBuilder
import java.util.*

class ResidentDataBuilder : AbstractDataBuilder<Resident>(Resident::class.java, 1) {

    override fun buildContent(container: DataView): Optional<Resident> {
        val uniqueId: UUID = container.getUUID(DataQueries.UNIQUE_ID).get()
        val name: String = container.getString(DataQueries.NAME).get()
        val townUniqueId: UUID? = container.getUUID(DataQueries.TOWN_UNIQUE_ID).orElse(null)

        val resident = ResidentImpl(
            uniqueId = uniqueId,
            name = name,
            townUniqueId = townUniqueId
        )

        return Optional.of(resident)
    }
}