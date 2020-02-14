package pw.dotdash.township.api.permission;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.annotation.CatalogedBy;

@CatalogedBy(ClaimPermissions.class)
public interface ClaimPermission extends Permission {

    static Builder builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    interface Builder extends Permission.Builder {

        @Override
        Builder id(@NonNull String id);

        @Override
        Builder name(@NonNull String name);

        @Override
        Builder from(Permission value);

        @Override
        Builder reset();

        @NonNull
        ClaimPermission build();
    }
}