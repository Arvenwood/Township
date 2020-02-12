package arvenwood.towns.api.town;

import arvenwood.towns.api.claim.Claim;
import arvenwood.towns.api.claim.ClaimService;
import arvenwood.towns.api.invite.Invite;
import arvenwood.towns.api.resident.Resident;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.util.Identifiable;
import org.spongepowered.api.util.ResettableBuilder;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.Optional;

public interface Town extends Identifiable, MessageReceiver {

    static Builder builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    String getName();

    void setName(String name);

    boolean isOpen();

    void setOpen(boolean open);

    Resident getOwner();

    void setOwner(Resident resident);

    Collection<Resident> getResidents();

    boolean addResident(Resident resident);

    boolean removeResident(Resident resident);

    Invite inviteResident(Resident sender, Resident receiver);

    default Collection<Claim> getClaims() {
        return ClaimService.get().getClaimsFor(this);
    }

    default Optional<Claim> getClaimAt(Location<World> location) {
        return ClaimService.get().getClaimAt(location)
                .filter(claim -> claim.getTown().equals(this));
    }

    interface Builder extends ResettableBuilder<Town, Builder> {

        Builder name(String name);

        Builder open(boolean open);

        Builder owner(Resident owner);

        Builder residents(Iterable<Resident> residents);

        Builder residents(Resident... residents);

        Town build();
    }
}