package arvenwood.towns.api.invite;

import arvenwood.towns.api.resident.Resident;
import arvenwood.towns.api.town.Town;
import org.spongepowered.api.Sponge;

import java.util.Collection;

public interface InviteService {

    static InviteService get() {
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