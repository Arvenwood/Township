package arvenwood.towns.plugin.event.invite

import arvenwood.towns.api.event.invite.CreateInviteEvent
import arvenwood.towns.api.invite.Invite
import org.spongepowered.api.event.cause.Cause

class CreateInviteEventImpl(
    invite: Invite,
    cause: Cause
) : AbstractTargetInviteEvent(invite, cause), CreateInviteEvent