package arvenwood.towns.plugin.resident

import arvenwood.towns.api.resident.Resident
import arvenwood.towns.plugin.storage.DataQueries
import arvenwood.towns.plugin.util.getUUID
import org.spongepowered.api.data.DataView
import org.spongepowered.api.data.persistence.AbstractDataBuilder
import org.spongepowered.api.data.persistence.DataBuilder
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