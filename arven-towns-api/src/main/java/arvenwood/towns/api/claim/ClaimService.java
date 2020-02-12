package arvenwood.towns.api.claim;

import arvenwood.towns.api.town.Town;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.Optional;

public interface ClaimService {

    static ClaimService get() {
        return Sponge.getServiceManager().provideUnchecked(ClaimService.class);
    }

    Optional<Claim> getClaimAt(Location<World> location);

    Collection<Claim> getClaimsFor(World world);

    Collection<Claim> getClaimsFor(Town town);

    boolean contains(Claim claim);

    boolean register(Claim claim);

    boolean unregister(Claim claim);
}