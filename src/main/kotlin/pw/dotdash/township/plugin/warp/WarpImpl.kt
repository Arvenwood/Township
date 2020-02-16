package pw.dotdash.township.plugin.warp

import pw.dotdash.township.api.town.Town
import pw.dotdash.township.api.warp.Warp
import pw.dotdash.township.plugin.storage.DataQueries
import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.data.DataView
import org.spongepowered.api.data.persistence.AbstractDataBuilder
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import pw.dotdash.township.api.town.TownService
import pw.dotdash.township.plugin.util.getUUID
import java.util.*

data class WarpImpl(
    private val uniqueId: UUID,
    private val name: String,
    private val location: Location<World>,
    private val townId: UUID
) : Warp {

    override fun getUniqueId(): UUID = this.uniqueId

    override fun getName(): String = this.name

    override fun getLocation(): Location<World> = this.location

    override fun getTownId(): UUID = this.townId

    override fun getTown(): Town =
        TownService.getInstance().getTown(this.townId)
            .orElseThrow { IllegalStateException("Town $townId is not loaded") }

    override fun getContentVersion(): Int = 1

    override fun toContainer(): DataContainer =
        DataContainer.createNew()
            .set(DataQueries.UNIQUE_ID, this.uniqueId)
            .set(DataQueries.NAME, this.name)
            .set(DataQueries.LOCATION, this.location)
            .set(DataQueries.TOWN_ID, this.townId)

    object DataBuilder : AbstractDataBuilder<Warp>(Warp::class.java, 1) {
        override fun buildContent(container: DataView): Optional<Warp> {
            val uniqueId: UUID = container.getUUID(DataQueries.UNIQUE_ID).get()
            val name: String = container.getString(DataQueries.NAME).get()
            val location: Location<World> = container.getSerializable(DataQueries.LOCATION, Location::class.java).get() as Location<World>
            val townId: UUID = container.getUUID(DataQueries.TOWN_ID).get()

            val warp = WarpImpl(
                uniqueId = uniqueId,
                name = name,
                location = location,
                townId = townId
            )

            return Optional.of(warp)
        }
    }

    class Builder : Warp.Builder {

        private var name: String? = null
        private var location: Location<World>? = null
        private var townId: UUID? = null

        override fun name(name: String): Warp.Builder {
            this.name = name
            return this
        }

        override fun location(location: Location<World>): Warp.Builder {
            this.location = location
            return this
        }

        override fun town(town: Town): Warp.Builder {
            this.townId = town.uniqueId
            return this
        }

        override fun from(value: Warp): Warp.Builder {
            this.name = value.name
            this.location = value.location
            this.townId = value.townId
            return this
        }

        override fun reset(): Warp.Builder {
            this.name = null
            this.location = null
            this.townId = null
            return this
        }

        override fun build(): Warp = WarpImpl(
            uniqueId = UUID.randomUUID(),
            name = checkNotNull(this.name),
            location = checkNotNull(this.location),
            townId = checkNotNull(this.townId)
        )
    }
}