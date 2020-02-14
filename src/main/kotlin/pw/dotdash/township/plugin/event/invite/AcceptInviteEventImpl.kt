package pw.dotdash.township.plugin.event.invite

import pw.dotdash.township.api.event.invite.AcceptInviteEvent
import pw.dotdash.township.api.invite.Invite
import org.spongepowered.api.event.cause.Cause

class AcceptInviteEventImpl(
    invite: Invite,
    cause: Cause
) : AbstractTargetInviteEvent(invite, cause), AcceptInviteEvent