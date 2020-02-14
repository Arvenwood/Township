package pw.dotdash.township.api.permission;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.annotation.CatalogedBy;

@CatalogedBy(TownPermissions.class)
public interface TownPermission extends Permission {

    static Builder builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    interface Builder extends Permission.Builder {

        @Override
        Builder id(String id);

        @Override
        Builder name(String name);

        @Override
        Builder from(Permission value);

        @Override
        Builder reset();

        TownPermission build();
    }
}