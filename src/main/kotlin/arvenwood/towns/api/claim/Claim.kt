package arvenwood.towns.api.claim

import arvenwood.towns.api.town.Town
import com.flowpowered.math.vector.Vector3i
import org.spongepowered.api.Sponge
import org.spongepowered.api.util.ResettableBuilder
import org.spongepowered.api.world.World

interface Claim {

    companion object {
        @JvmStatic
        fun builder(): Builder =
            Sponge.getRegistry().createBuilder(Builder::class.java)
    }

    val world: World

    val chunkPosition: Vector3i

    val town: Town

    interface Builder : ResettableBuilder<Claim, Builder> {

        fun world(world: World): Builder

        fun chunkPosition(chunkPosition: Vector3i): Builder

        fun town(town: Town): Builder

        fun build(): Claim
    }
}
