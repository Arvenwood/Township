package pw.dotdash.township.api.invite;

import pw.dotdash.township.api.resident.Resident;
import pw.dotdash.township.api.town.Town;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.ResettableBuilder;

import java.time.Instant;

public interface Invite {

    static Builder builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    Resident getSender();

    Resident getReceiver();

    Town getTown();

    Instant getCreationTime();

    boolean isAccepted();

    void setAccepted(boolean accepted);

    interface Builder extends ResettableBuilder<Invite, Builder> {

        Builder sender(Resident sender);

        Builder receiver(Resident receiver);

        Builder town(Town town);

        Builder creationTime(Instant creationTime);

        Invite build();
    }
}