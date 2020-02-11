package arvenwood.towns.api.town;

import arvenwood.towns.api.claim.Claim;
import arvenwood.towns.api.claim.ClaimService;
import arvenwood.towns.api.resident.Resident;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.Identifiable;
import org.spongepowered.api.util.ResettableBuilder;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.Optional;

public interface Town extends Identifiable {

    static Builder builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    String getName();

    void setName(String name);

    Resident getOwner();

    void setOwner(Resident resident);

    Collection<Resident> getResidents();

    boolean addResident(Resident resident);

    boolean removeResident(Resident resident);

    default Collection<Claim> getClaims() {
        return ClaimService.get().getClaimsFor(this);
    }

    default Optional<Claim> getClaimAt(Location<World> location) {
        return ClaimService.get().getClaimAt(location)
                .filter(claim -> claim.getTown().equals(this));
    }

    interface Builder extends ResettableBuilder<Town, Builder> {

        Builder name(String name);

        Builder owner(Resident owner);

        Builder residents(Iterable<Resident> residents);

        Builder residents(Resident... residents);

        Town build();
    }
}