package pw.dotdash.township.api.town;

import org.spongepowered.api.Sponge;
import pw.dotdash.township.api.permission.Role;

import java.util.UUID;

/**
 * An object which can hold permission data, with {@link Town} association.
 */
public interface TownRole extends Role {

    /**
     * Creates a new {@link Builder} to build a {@link TownRole}.
     *
     * @return The new builder
     */
    static Builder builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    /**
     * Gets the {@link UUID} of the town this role is associated with.
     *
     * @return The associated town's id
     */
    UUID getTownId();

    /**
     * Gets the {@link Town} this role is associated with.
     *
     * @return The associated town
     */
    Town getTown();

    interface Builder extends Role.Builder<TownRole, Builder> {

        /**
         * Sets the associated town of the role.
         *
         * @param town The town to set
         * @return This builder, for chaining
         */
        Builder town(Town town);
    }
}