package arvenwood.towns.plugin.event.invite

import arvenwood.towns.api.event.invite.AcceptInviteEvent
import arvenwood.towns.api.invite.Invite
import org.spongepowered.api.event.cause.Cause

class AcceptInviteEventImpl(
    invite: Invite,
    cause: Cause
) : AbstractTargetInviteEvent(invite, cause), AcceptInviteEvent