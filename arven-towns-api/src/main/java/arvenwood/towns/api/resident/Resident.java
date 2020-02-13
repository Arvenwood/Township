package arvenwood.towns.api.resident;

import arvenwood.towns.api.town.Town;
import arvenwood.towns.api.town.TownService;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.util.Identifiable;

import java.util.Optional;
import java.util.UUID;

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

    default boolean isOwner() {
        return this.getTown().filter(town -> town.getOwner().equals(this)).isPresent();
    }

    default Optional<Player> getPlayer() {
        return Sponge.getServer().getPlayer(this.getUniqueId());
    }

    Optional<? extends Account> getAccount();
}