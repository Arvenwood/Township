package arvenwood.towns.plugin.claim

import arvenwood.towns.api.claim.Claim
import arvenwood.towns.api.town.Town
import com.flowpowered.math.vector.Vector3i
import org.spongepowered.api.world.World

data class ClaimImpl(
    private val world: World,
    private val chunkPosition: Vector3i,
    private val town: Town
) : Claim {

    override fun getWorld(): World = this.world

    override fun getChunkPosition(): Vector3i = this.chunkPosition

    override fun getTown(): Town = this.town

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
            world = checkNotNull(this.world),
            chunkPosition = checkNotNull(this.chunkPosition),
            town = checkNotNull(this.town)
        )
    }
}