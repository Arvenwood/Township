package pw.dotdash.township.plugin.event.invite

import pw.dotdash.township.api.event.invite.DeleteInviteEvent
import pw.dotdash.township.api.invite.Invite
import org.spongepowered.api.event.cause.Cause

class DeleteInviteEventImpl(
    invite: Invite,
    cause: Cause
) : AbstractTargetInviteEvent(invite, cause), DeleteInviteEvent