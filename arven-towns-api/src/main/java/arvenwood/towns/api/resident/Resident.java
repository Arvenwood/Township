package arvenwood.towns.api.resident;

import arvenwood.towns.api.town.Town;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.Identifiable;

import java.util.Optional;
import java.util.UUID;

public interface Resident extends Identifiable {

    @Override
    UUID getUniqueId();

    String getName();

    Optional<Town> getTown();

    void setTown(@Nullable Town town);

    default boolean isOwner() {
        return this.getTown().filter(town -> town.getOwner().equals(this)).isPresent();
    }

    default Optional<Player> getPlayer() {
        return Sponge.getServer().getPlayer(this.getUniqueId());
    }
}