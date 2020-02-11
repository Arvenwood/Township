package arvenwood.towns.api.claim;

import arvenwood.towns.api.town.Town;
import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.ResettableBuilder;
import org.spongepowered.api.world.World;

public interface Claim {

    static Builder builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    World getWorld();

    Vector3i getChunkPosition();

    Town getTown();

    interface Builder extends ResettableBuilder<Claim, Builder> {

        Builder world(World world);

        Builder chunkPosition(Vector3i chunkPosition);

        Builder town(Town town);

        Claim build();
    }
}