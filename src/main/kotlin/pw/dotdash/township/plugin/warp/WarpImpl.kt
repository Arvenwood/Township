package pw.dotdash.township.plugin.warp

import pw.dotdash.township.api.town.Town
import pw.dotdash.township.api.warp.Warp
import pw.dotdash.township.plugin.storage.DataQueries
import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.data.DataView
import org.spongepowered.api.data.persistence.AbstractDataBuilder
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import pw.dotdash.township.plugin.util.getUUID
import java.util.*

data class WarpImpl(
    private val uniqueId: UUID,
    private val name: String,
    private val location: Location<World>,
    private val townUniqueId: UUID
) : Warp {

    override fun getUniqueId(): UUID = this.uniqueId

    override fun getName(): String = this.name

    override fun getLocation(): Location<World> = this.location

    override fun getTownUniqueId(): UUID = this.townUniqueId

    override fun getContentVersion(): Int = 1

    override fun toContainer(): DataContainer =
        DataContainer.createNew()
            .set(DataQueries.UNIQUE_ID, this.uniqueId)
            .set(DataQueries.NAME, this.name)
            .set(DataQueries.LOCATION, this.location)
            .set(DataQueries.TOWN_UNIQUE_ID, this.townUniqueId)

    object DataBuilder : AbstractDataBuilder<Warp>(Warp::class.java, 1) {
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

    class Builder : Warp.Builder {

        private var name: String? = null
        private var location: Location<World>? = null
        private var town: Town? = null

        override fun name(name: String): Warp.Builder {
            this.name = name
            return this
        }

        override fun location(location: Location<World>): Warp.Builder {
            this.location = location
            return this
        }

        override fun town(town: Town): Warp.Builder {
            this.town = town
            return this
        }

        override fun from(value: Warp): Warp.Builder {
            this.name = value.name
            this.location = value.location
            this.town = value.town
            return this
        }

        override fun reset(): Warp.Builder {
            this.name = null
            this.location = null
            this.town = null
            return this
        }

        override fun build(): Warp = WarpImpl(
            uniqueId = UUID.randomUUID(),
            name = checkNotNull(this.name),
            location = checkNotNull(this.location),
            townUniqueId = checkNotNull(this.town).uniqueId
        )
    }
}