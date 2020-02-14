package pw.dotdash.township.plugin.warp

import pw.dotdash.township.api.warp.Warp
import pw.dotdash.township.plugin.storage.DataQueries
import pw.dotdash.township.plugin.util.getUUID
import org.spongepowered.api.data.DataView
import org.spongepowered.api.data.persistence.AbstractDataBuilder
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import java.util.*

class WarpDataBuilder : AbstractDataBuilder<Warp>(Warp::class.java, 1) {

    override fun buildContent(container: DataView): Optional<Warp> {
        val uniqueId: UUID = container.getUUID(DataQueries.UNIQUE_ID).get()
        val name: String = container.getString(DataQueries.NAME).get()
        val location: Location<World> = container.getSerializable(DataQueries.LOCATION, Location::class.java).get() as Location<World>
        val townUniqueId: UUID = container.getUUID(DataQueries.TOWN_UNIQUE_ID).get()

        val warp = WarpImpl(
            uniqueId = uniqueId,
            name = name,
            location = location,
            townUniqueId = townUniqueId
        )

        return Optional.of(warp)
    }
}