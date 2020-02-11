package arvenwood.towns.plugin.claim

import arvenwood.towns.api.claim.Claim
import arvenwood.towns.api.town.Town
import com.flowpowered.math.vector.Vector3i
import org.spongepowered.api.world.World

data class SimpleClaim(
    override val world: World,
    override val chunkPosition: Vector3i,
    override val town: Town
) : Claim {

    class Builder : Claim.Builder {

        private var world: World? = null
        private var chunkPosition: Vector3i? = null
        private var town: Town? = null

        override fun world(world: World): Claim.Builder {
            this.world = world
            return this
        }

        override fun chunkPosition(chunkPosition: Vector3i): Claim.Builder {
            this.chunkPosition = chunkPosition
            return this
        }

        override fun town(town: Town): Claim.Builder {
            this.town = town
            return this
        }

        override fun from(value: Claim): Claim.Builder {
            this.world = value.world
            this.chunkPosition = value.chunkPosition
            this.town = value.town
            return this
        }

        override fun reset(): Claim.Builder {
            this.world = null
            this.chunkPosition = null
            this.town = null
            return this
        }

        override fun build(): Claim = SimpleClaim(
            world = checkNotNull(this.world),
            chunkPosition = checkNotNull(this.chunkPosition),
            town = checkNotNull(this.town)
        )
    }
}