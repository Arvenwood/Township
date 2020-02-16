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
import pw.dotdash.township.api.nation.Nation;
import pw.dotdash.township.api.permission.ClaimPermission;
import pw.dotdash.township.api.resident.Resident;
import pw.dotdash.township.api.warp.Warp;
import pw.dotdash.township.api.warp.WarpService;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

public interface Town extends Identifiable, MessageReceiver, DataSerializable {

    /**
     * Creates a new {@link Builder} to build a {@link Town}.
     *
     * @return The new builder
     */
    static Builder builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    /**
     * Gets the name of the town.
     *
     * @return The name of the town
     */
    String getName();

    /**
     * Sets the name of the town.
     *
     * @param name The name to set
     * @return True if the action was successful
     */
    boolean setName(String name);

    /**
     * Checks if the town is joinable without an invitation.
     *
     * @return True if the town is joinable without an invite
     */
    boolean isOpen();

    /**
     * Sets whether the town is joinable without an invitation.
     *
     * @param open True if joinable without an invite
     * @return True if the action was successful
     */
    boolean setOpen(boolean open);

    /**
     * Gets the owner of the town.
     *
     * @return The owner of the town
     */
    Resident getOwner();

    /**
     * Checks if the specified resident is the owner of the town.
     *
     * @param resident The resident to check
     * @return True if the resident is the owner
     */
    boolean isOwner(Resident resident);

    /**
     * Sets the owner of the town.
     *
     * @param resident The resident to set
     * @return True if the action was successful
     */
    boolean setOwner(Resident resident);

    /**
     * Gets the nation the town is part of, if available.
     *
     * @return The nation, if available
     */
    Optional<Nation> getNation();

    /**
     * Checks if the town is in a nation.
     *
     * @return True if the town is in a nation
     */
    boolean hasNation();

    /**
     * Sets the nation the town is a part of.
     *
     * @param nation The nation to set
     * @return True if the action was successful
     */
    boolean setNation(@Nullable Nation nation);

    /**
     * Gets all residents in the town.
     *
     * @return All residents in the town
     */
    Collection<Resident> getResidents();

    /**
     * Checks if the specified resident is a member of the town.
     *
     * @param resident The resident to check
     * @return True if the resident is a member
     */
    boolean hasResident(Resident resident);

    /**
     * Adds the specified resident as a member of the town.
     *
     * @param resident The resident to add
     * @return True if the action was successful
     */
    boolean addResident(Resident resident);

    /**
     * Removes the specified resident as a member of the town.
     *
     * @param resident The resident to remove
     * @return True if the action was successful
     */
    boolean removeResident(Resident resident);

    /**
     * Gets all claims registered to the town.
     *
     * @return All registered claims
     */
    default Collection<Claim> getClaims() {
        return ClaimService.getInstance().getClaimsFor(this);
    }

    /**
     * Gets the claim registered to the town at the specified location,
     * if available.
     *
     * @param location The location to check
     * @return The claim, if available
     */
    default Optional<Claim> getClaimAt(Location<World> location) {
        return ClaimService.getInstance().getClaimAt(location)
                .filter(claim -> claim.getTown().equals(this));
    }

    /**
     * Gets the role used for permission checks of non-residents.
     *
     * @return The non-residents role
     */
    TownRole getVisitorRole();

    /**
     * Gets all roles registered to the town.
     *
     * @return All registered roles
     */
    default Collection<TownRole> getRoles() {
        return TownRoleService.getInstance().getRoles(this);
    }

    /**
     * Gets the role with the specified name that's registered to the town,
     * if available.
     *
     * @param name The name to find
     * @return The role, if available
     */
    default Optional<TownRole> getRole(String name) {
        return TownRoleService.getInstance().getRole(this, name);
    }

    /**
     * Gets all warps registered to the town.
     *
     * @return All registered roles
     */
    default Collection<Warp> getWarps() {
        return WarpService.getInstance().getWarpsByTown(this);
    }

    /**
     * Gets the warp with the specified name that's registered to the town,
     * if available.
     *
     * @param name The name to find
     * @return The role, if available
     */
    default Optional<Warp> getWarp(String name) {
        return WarpService.getInstance().getWarp(this, name);
    }

    /**
     * Gets the {@link Account} associated with the town, if available.
     *
     * @return The associated account, if available
     */
    Optional<Account> getAccount();

    /**
     * Gets the town's current balance in the specified currency.
     *
     * @param currency The currency to check
     * @return The town's current balance
     */
    BigDecimal getBalance(Currency currency);

    /**
     * Gets the town's current balance in the default currency.
     *
     * @return The town's current balance
     */
    BigDecimal getBalance();

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