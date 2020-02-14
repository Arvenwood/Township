package pw.dotdash.township.api.permission;

import org.spongepowered.api.CatalogType;
import org.spongepowered.api.util.ResettableBuilder;

public interface Permission extends CatalogType {

    interface Builder extends ResettableBuilder<Permission, Builder> {

        Builder id(String id);

        Builder name(String name);
    }
}