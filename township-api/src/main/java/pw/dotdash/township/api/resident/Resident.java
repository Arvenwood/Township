package pw.dotdash.township.api.resident;

import pw.dotdash.township.api.town.Town;
import pw.dotdash.township.api.town.TownService;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.util.Identifiable;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public interface Resident extends Identifiable, MessageReceiver, DataSerializable {

    @Override
    UUID getUniqueId();

    String getName();

    Optional<UUID> getTownUniqueId();

    default Optional<Town> getTown() {
        return getTownUniqueId()
                .flatMap(uuid -> TownService.getInstance().getTown(uuid));
    }

    boolean setTown(@Nullable Town town);

    Collection<UUID> getFriendUniqueIds();

    default Collection<Resident> getFriends() {
        return getFriendUniqueIds().stream()
                .map(uuid -> ResidentService.getInstance().getResident(uuid).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    default boolean isOwner() {
        return this.getTown().filter(town -> town.getOwner().equals(this)).isPresent();
    }

    default Optional<Player> getPlayer() {
        return Sponge.getServer().getPlayer(this.getUniqueId());
    }

    Optional<? extends Account> getAccount();
}