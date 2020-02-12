package arvenwood.towns.api.event.invite;

import arvenwood.towns.api.invite.Invite;
import org.spongepowered.api.event.Event;

public interface TargetInviteEvent extends Event {

    Invite getInvite();
}