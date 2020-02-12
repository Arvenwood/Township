package arvenwood.towns.plugin.event.invite

import arvenwood.towns.api.event.invite.TargetInviteEvent
import arvenwood.towns.api.invite.Invite
import org.spongepowered.api.event.Cancellable
import org.spongepowered.api.event.cause.Cause

abstract class AbstractTargetInviteEvent(
    private val invite: Invite,
    private val cause: Cause
) : TargetInviteEvent, Cancellable {

    private var cancelled: Boolean = false

    override fun isCancelled(): Boolean = this.cancelled

    override fun setCancelled(cancel: Boolean) {
        this.cancelled = cancel
    }

    override fun getInvite(): Invite = this.invite

    override fun getCause(): Cause = this.cause
}