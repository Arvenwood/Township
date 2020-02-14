package pw.dotdash.township.api.role;

import org.spongepowered.api.Sponge;
import pw.dotdash.township.api.town.Town;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface TownRoleService {

    static TownRoleService getInstance() {
        return Sponge.getServiceManager().provideUnchecked(TownRoleService.class);
    }

    Collection<TownRole> getRoles();

    Optional<TownRole> getRole(UUID uniqueId);

    Collection<TownRole> getRoles(Town town);

    Optional<TownRole> getRole(Town town, String name);

    boolean contains(TownRole role);

    boolean register(TownRole role);

    boolean unregister(TownRole role);
}