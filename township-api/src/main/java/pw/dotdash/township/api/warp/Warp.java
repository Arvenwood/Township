package pw.dotdash.township.api.warp;

import pw.dotdash.township.api.town.Town;
import pw.dotdash.township.api.town.TownService;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.util.Identifiable;
import org.spongepowered.api.util.ResettableBuilder;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.UUID;

public interface Warp extends Identifiable, DataSerializable {

    String getName();

    Location<World> getLocation();

    UUID getTownUniqueId();

    default Town getTown() {
        return TownService.getInstance()
                .getTown(this.getTownUniqueId())
                .orElseThrow(() -> new RuntimeException("Town " + this.getTownUniqueId() + " is not loaded"));
    }

    interface Builder extends ResettableBuilder<Warp, Builder> {

        Builder name(String name);

        Builder location(Location<World> location);

        Builder town(Town town);

        Warp build();
    }
}