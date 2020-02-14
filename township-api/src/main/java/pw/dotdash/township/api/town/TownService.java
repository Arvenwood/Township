package pw.dotdash.township.api.town;

import org.spongepowered.api.Sponge;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface TownService {

    static TownService getInstance() {
        return Sponge.getServiceManager().provideUnchecked(TownService.class);
    }

    Collection<Town> getTowns();

    Optional<Town> getTown(UUID uniqueId);

    Optional<Town> getTown(String name);

    boolean contains(Town town);

    boolean register(Town town);

    boolean unregister(Town town);
}