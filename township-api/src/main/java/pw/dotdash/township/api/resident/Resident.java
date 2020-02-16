package pw.dotdash.township.api.resident;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.util.Identifiable;
import org.spongepowered.api.util.Tristate;
import pw.dotdash.township.api.claim.Claim;
import pw.dotdash.township.api.claim.ClaimService;
import pw.dotdash.township.api.permission.Permission;
import pw.dotdash.township.api.town.TownRole;
import pw.dotdash.township.api.town.Town;

import java.util.Collection;
import java.util.Optional;

/**
 * A {@link Resident} is the data usually associated with a {@link Player},
 * within the confines of Township.
 */
public interface Resident extends Identifiable, MessageReceiver, DataSerializable {

    /**
     * Gets the name of this resident.
     *
     * @return The name of this resident
     */
    String getName();

    /**
     * Gets the town that this resident resides in, if available.
     *
     * @return This resident's town
     */
    Optional<Town> getTown();

    /**
     * Returns true if this resident resides in a town.
     *
     * @return Whether this resident resides in a town
     */
    boolean hasTown();

    /**
     * Sets the town that this resident resides in.
     *
     * @param town The town to join them to, or null to leave their current
     * @return Whether the action was successful
     * @see pw.dotdash.township.api.event.town.ChangeTownEvent.Join
     * @see pw.dotdash.township.api.event.town.ChangeTownEvent.Leave
     */
    boolean setTown(@Nullable Town town);

    /**
     * Gets this resident's friends.
     *
     * @return This resident's friends
     */
    Collection<Resident> getFriends();

    /**
     * Returns true if the specified resident is this resident's friend.
     *
     * @param resident The resident to check
     * @return Whether the specified resident is this resident's friend
     */
    boolean hasFriend(Resident resident);

    /**
     * Adds the specified resident as a friend of this resident.
     *
     * @param resident The resident to add
     * @return Whether the action was successful
     */
    boolean addFriend(Resident resident);

    /**
     * Removes the specified resident as a friend of this resident.
     *
     * @param resident The resident to remove
     * @return Whether the action was successful
     */
    boolean removeFriend(Resident resident);

    /**
     * Gets this resident's town roles.
     *
     * @return This resident's town roles
     */
    Collection<TownRole> getTownRoles();

    /**
     * Returns true if this resident has the specified town role.
     *
     * @param role The town role to check
     * @return Whether this resident has the specified town role
     */
    boolean hasRole(TownRole role);

    /**
     * Adds the specified town role to this resident.
     *
     * @param role The town role to add
     * @return Whether the action was successful
     */
    boolean addRole(TownRole role);

    /**
     * Removes the specified town role to this resident.
     *
     * @param role The town role to remove
     * @return Whether the action was successful
     */
    boolean removeRole(TownRole role);

    Tristate getPermissionValue(Permission permission, Claim claim);

    default boolean hasPermission(Permission permission, Claim claim) {
        return this.getPermissionValue(permission, claim).asBoolean();
    }

    default Tristate getPermissionValue(Permission permission) {
        return this.getPlayer()
                .flatMap(player -> ClaimService.getInstance().getClaimAt(player.getLocation()))
                .map(claim -> this.getPermissionValue(permission, claim))
                .orElse(Tristate.UNDEFINED);
    }

    default boolean hasPermission(Permission permission) {
        return this.getPermissionValue(permission).asBoolean();
    }

    /**
     * Returns true if this resident is the owner of the town they reside in.
     *
     * @return Whether this resident owns the town
     */
    default boolean isOwner() {
        return this.getTown().filter(town -> town.getOwner().equals(this)).isPresent();
    }

    /**
     * Gets the {@link Player} associated with this resident, if available.
     *
     * @return The associated player, if available
     */
    default Optional<Player> getPlayer() {
        return Sponge.getServer().getPlayer(this.getUniqueId());
    }

    /**
     * Gets the {@link Account} associated with this resident, if available.
     *
     * @return The associated account, if available
     */
    default Optional<? extends Account> getAccount() {
        return Sponge.getServiceManager().provide(EconomyService.class)
                .flatMap(service -> service.getOrCreateAccount(this.getUniqueId()));
    }
}