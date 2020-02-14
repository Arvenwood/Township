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
import pw.dotdash.township.api.role.Role;
import pw.dotdash.township.api.role.TownRole;
import pw.dotdash.township.api.town.Town;
import pw.dotdash.township.api.town.TownService;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface Resident extends Identifiable, MessageReceiver, DataSerializable {

    String getName();

    Optional<UUID> getTownUniqueId();

    default Optional<Town> getTown() {
        return getTownUniqueId().flatMap(uuid -> TownService.getInstance().getTown(uuid));
    }

    boolean setTown(@Nullable Town town);

    Collection<UUID> getFriendIds();

    Collection<Resident> getFriends();

    Collection<UUID> getTownRoleIds();

    Collection<TownRole> getTownRoles();

    boolean hasRole(TownRole role);

    boolean addRole(TownRole role);

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

    default boolean isOwner() {
        return this.getTown().filter(town -> town.getOwner().equals(this)).isPresent();
    }

    default Optional<Player> getPlayer() {
        return Sponge.getServer().getPlayer(this.getUniqueId());
    }

    default Optional<? extends Account> getAccount() {
        return Sponge.getServiceManager().provide(EconomyService.class)
                .flatMap(service -> service.getOrCreateAccount(this.getUniqueId()));
    }
}