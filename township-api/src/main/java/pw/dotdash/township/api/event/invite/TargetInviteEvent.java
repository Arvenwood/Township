package pw.dotdash.township.api.event.invite;

import pw.dotdash.township.api.invite.Invite;
import org.spongepowered.api.event.Event;

public interface TargetInviteEvent extends Event {

    Invite getInvite();
}