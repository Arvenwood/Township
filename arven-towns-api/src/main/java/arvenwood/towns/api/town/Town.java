package arvenwood.towns.api.town;

import arvenwood.towns.api.claim.Claim;
import arvenwood.towns.api.claim.ClaimService;
import arvenwood.towns.api.invite.Invite;
import arvenwood.towns.api.resident.Resident;
import arvenwood.towns.api.warp.Warp;
import arvenwood.towns.api.warp.WarpService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.util.Identifiable;
import org.spongepowered.api.util.ResettableBuilder;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

public interface Town extends Identifiable, MessageReceiver, DataSerializable {

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
        return ClaimService.getInstance().getClaimsFor(this);
    }

    default Optional<Claim> getClaimAt(Location<World> location) {
        return ClaimService.getInstance().getClaimAt(location)
                .filter(claim -> claim.getTown().equals(this));
    }

    default Collection<Warp> getWarps() {
        return WarpService.getInstance().getWarpsByTown(this);
    }

    Optional<Account> getAccount();

    default BigDecimal getBalance(Currency currency) {
        return this.getAccount()
                .map(account -> account.getBalance(currency))
                .orElse(BigDecimal.ZERO);
    }

    default BigDecimal getBalance() {
        return this.getAccount()
                .map(account -> account.getBalance(Sponge.getServiceManager()
                        .provideUnchecked(EconomyService.class)
                        .getDefaultCurrency()))
                .orElse(BigDecimal.ZERO);
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