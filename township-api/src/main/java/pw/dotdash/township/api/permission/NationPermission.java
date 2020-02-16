package pw.dotdash.township.api.permission;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.annotation.CatalogedBy;

@CatalogedBy(NationPermissions.class)
public interface NationPermission extends Permission {

    static Builder builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    interface Builder extends Permission.Builder<NationPermission, Builder> {

    }
}