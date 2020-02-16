package pw.dotdash.township.api.nation;

import org.spongepowered.api.Sponge;
import pw.dotdash.township.api.town.Town;
import pw.dotdash.township.api.town.TownRole;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface NationRoleService {

    static NationRoleService getInstance() {
        return Sponge.getServiceManager().provideUnchecked(NationRoleService.class);
    }

    Collection<NationRole> getRoles();

    Optional<NationRole> getRole(UUID uniqueId);

    Collection<NationRole> getRoles(Nation nation);

    Optional<NationRole> getRole(Nation nation, String name);

    boolean contains(NationRole role);

    boolean register(NationRole role);

    boolean unregister(NationRole role);
}