package arvenwood.towns.plugin.claim

import arvenwood.towns.api.claim.Claim
import arvenwood.towns.api.town.Town
import arvenwood.towns.plugin.storage.DataQueries
import com.flowpowered.math.vector.Vector3i
import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.data.DataQuery
import org.spongepowered.api.world.World
import java.util.*

data class ClaimImpl(
    private val worldUniqueId: UUID,
    private val chunkPosition: Vector3i,
    private val townUniqueId: UUID
) : Claim {

    override fun getWorldUniqueId(): UUID = this.worldUniqueId

    override fun getChunkPosition(): Vector3i = this.chunkPosition

    override fun getTownUniqueId(): UUID = this.townUniqueId

    override fun getContentVersion(): Int = 1

    override fun toContainer(): DataContainer =
        DataContainer.createNew()
            .set(DataQueries.WORLD_UNIQUE_ID, this.worldUniqueId.toString())
            .set(DataQueries.CHUNK_POSITION_X, this.chunkPosition.x)
            .set(DataQueries.CHUNK_POSITION_Y, this.chunkPosition.y)
            .set(DataQueries.CHUNK_POSITION_Z, this.chunkPosition.z)
            .set(DataQueries.TOWN_UNIQUE_ID, this.townUniqueId.toString())

    class Builder : Claim.Builder {

        private var world: World? = null
        private var chunkPosition: Vector3i? = null
        private var town: Town? = null

        override fun world(world: World): Builder {
            this.world = world
            return this
        }

        override fun chunkPosition(chunkPosition: Vector3i): Builder {
            this.chunkPosition = chunkPosition
            return this
        }

        override fun town(town: Town): Builder {
            this.town = town
            return this
        }

        override fun from(value: Claim): Builder {
            this.world = value.world
            this.chunkPosition = value.chunkPosition
            this.town = value.town
            return this
        }

        override fun reset(): Builder {
            this.world = null
            this.chunkPosition = null
            this.town = null
            return this
        }

        override fun build(): Claim = ClaimImpl(
            worldUniqueId = checkNotNull(this.world).uniqueId,
            chunkPosition = checkNotNull(this.chunkPosition),
            townUniqueId = checkNotNull(this.town).uniqueId
        )
    }
}