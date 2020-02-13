package arvenwood.towns.api.warp;

import arvenwood.towns.api.town.Town;
import org.spongepowered.api.Sponge;

import java.util.Collection;
import java.util.Optional;

public interface WarpService {

    static WarpService getInstance() {
        return Sponge.getServiceManager().provideUnchecked(WarpService.class);
    }

    Collection<Warp> getWarps();

    Collection<Warp> getWarpsByTown(Town town);

    Optional<Warp> getWarp(Town town, String name);

    boolean contains(Warp warp);

    boolean register(Warp warp);

    boolean unregister(Warp warp);
}