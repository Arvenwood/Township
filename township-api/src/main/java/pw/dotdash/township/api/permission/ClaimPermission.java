package pw.dotdash.township.api.permission;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.annotation.CatalogedBy;

@CatalogedBy(ClaimPermissions.class)
public interface ClaimPermission extends Permission {

    static Builder builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    interface Builder extends Permission.Builder<ClaimPermission, Builder> {

    }
}