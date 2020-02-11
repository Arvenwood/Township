package arvenwood.towns.api.resident;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ResidentService {

    static ResidentService get() {
        return Sponge.getServiceManager().provideUnchecked(ResidentService.class);
    }

    Collection<Resident> getResidents();

    Optional<Resident> getResident(UUID uniqueId);

    Optional<Resident> getResident(String name);

    Resident getOrCreateResident(Player player);
}