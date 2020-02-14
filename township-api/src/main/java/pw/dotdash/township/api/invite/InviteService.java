package pw.dotdash.township.api.invite;

import pw.dotdash.township.api.resident.Resident;
import pw.dotdash.township.api.town.Town;
import org.spongepowered.api.Sponge;

import java.util.Collection;

public interface InviteService {

    static InviteService getInstance() {
        return Sponge.getServiceManager().provideUnchecked(InviteService.class);
    }

    Collection<Invite> getInvitesBySender(Resident sender);

    Collection<Invite> getInvitesByReceiver(Resident receiver);

    Collection<Invite> getInvitesByTown(Town town);

    boolean contains(Invite invite);

    boolean register(Invite invite);

    boolean unregister(Invite invite);

    boolean accept(Invite invite);
}