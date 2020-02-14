package pw.dotdash.township.api.role;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.util.Identifiable;
import org.spongepowered.api.util.ResettableBuilder;
import pw.dotdash.township.api.permission.Permission;
import pw.dotdash.township.api.town.Town;
import pw.dotdash.township.api.town.TownService;

import java.util.Collection;
import java.util.UUID;

public interface TownRole extends Role {

    static Builder builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    UUID getTownUniqueId();

    default Town getTown() {
        return TownService.getInstance()
                .getTown(this.getTownUniqueId())
                .orElseThrow(() -> new IllegalStateException("Town " + this.getTownUniqueId() + " is not loaded"));
    }

    interface Builder extends ResettableBuilder<TownRole, Builder> {

        Builder name(String name);

        Builder town(Town town);

        Builder permissions(Iterable<Permission> permissions);

        Builder permissions(Permission... permissions);

        TownRole build();
    }
}