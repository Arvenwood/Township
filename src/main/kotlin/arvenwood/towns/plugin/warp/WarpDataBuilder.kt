package arvenwood.towns.plugin.warp

import arvenwood.towns.api.warp.Warp
import arvenwood.towns.plugin.storage.DataQueries
import arvenwood.towns.plugin.util.getUUID
import org.spongepowered.api.data.DataView
import org.spongepowered.api.data.persistence.AbstractDataBuilder
import org.spongepowered.api.data.persistence.DataBuilder
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