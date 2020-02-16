package pw.dotdash.township.api.warp;

import org.spongepowered.api.Sponge;
import pw.dotdash.township.api.town.Town;
import pw.dotdash.township.api.town.TownService;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.util.Identifiable;
import org.spongepowered.api.util.ResettableBuilder;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.UUID;

public interface Warp extends Identifiable, DataSerializable {

    /**
     * Creates a new {@link Builder} to build a {@link Warp}.
     *
     * @return The new builder
     */
    static Builder builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    /**
     * Gets the name of the warp.
     *
     * @return The name of the warp
     */
    String getName();

    /**
     * Gets the location where players are warped to.
     *
     * @return The warp location
     */
    Location<World> getLocation();

    /**
     * Gets the {@link UUID} of the town the warp is associated with.
     *
     * @return The associated town's id
     */
    UUID getTownId();

    /**
     * Gets the town the warp is associated with.
     *
     * @return The associated town
     */
    Town getTown();

    interface Builder extends ResettableBuilder<Warp, Builder> {

        Builder name(String name);

        Builder location(Location<World> location);

        Builder town(Town town);

        Warp build();
    }
}