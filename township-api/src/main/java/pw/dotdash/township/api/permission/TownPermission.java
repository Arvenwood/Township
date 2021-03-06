package pw.dotdash.township.api.permission;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.annotation.CatalogedBy;

@CatalogedBy(TownPermissions.class)
public interface TownPermission extends Permission {

    static Builder builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    interface Builder extends Permission.Builder<TownPermission, Builder> {

    }
}