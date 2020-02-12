package arvenwood.towns.plugin.event.invite

import arvenwood.towns.api.event.invite.DeleteInviteEvent
import arvenwood.towns.api.invite.Invite
import org.spongepowered.api.event.cause.Cause

class DeleteInviteEventImpl(
    invite: Invite,
    cause: Cause
) : AbstractTargetInviteEvent(invite, cause), DeleteInviteEvent