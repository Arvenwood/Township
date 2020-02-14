package pw.dotdash.township.plugin.event.invite

import pw.dotdash.township.api.event.invite.CreateInviteEvent
import pw.dotdash.township.api.invite.Invite
import org.spongepowered.api.event.cause.Cause

class CreateInviteEventImpl(
    invite: Invite,
    cause: Cause
) : AbstractTargetInviteEvent(invite, cause), CreateInviteEvent