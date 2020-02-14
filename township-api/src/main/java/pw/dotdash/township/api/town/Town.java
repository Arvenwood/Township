package pw.dotdash.township.api.town;

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
import pw.dotdash.township.api.claim.Claim;
import pw.dotdash.township.api.claim.ClaimService;
import pw.dotdash.township.api.invite.Invite;
import pw.dotdash.township.api.permission.ClaimPermission;
import pw.dotdash.township.api.resident.Resident;
import pw.dotdash.township.api.role.TownRole;
import pw.dotdash.township.api.role.TownRoleService;
import pw.dotdash.township.api.warp.Warp;
import pw.dotdash.township.api.warp.WarpService;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface Town extends Identifiable, MessageReceiver, DataSerializable {

    static Builder builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    String getName();

    boolean setName(String name);

    boolean isOpen();

    void setOpen(boolean open);

    Resident getOwner();

    void setOwner(Resident resident);

    Collection<UUID> getResidentIds();

    Collection<Resident> getResidents();

    boolean hasResident(Resident resident);

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

    TownRole getVisitorRole();

    default Collection<TownRole> getRoles() {
        return TownRoleService.getInstance().getRoles(this);
    }

    default Optional<TownRole> getRole(String name) {
        return TownRoleService.getInstance().getRole(this, name);
    }

    default Collection<Warp> getWarps() {
        return WarpService.getInstance().getWarpsByTown(this);
    }

    default Optional<Warp> getWarp(String name) {
        return WarpService.getInstance().getWarp(this, name);
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

        Builder addVisitorRolePermission(ClaimPermission permission);

        Town build();
    }
}