package arvenwood.towns.plugin.event.claim

import arvenwood.towns.api.claim.Claim
import arvenwood.towns.api.event.claim.DeleteClaimEvent
import org.spongepowered.api.event.cause.Cause

data class DeleteClaimEventImpl(
    private val claim: Claim,
    private val cause: Cause
) : DeleteClaimEvent {

    private var cancelled: Boolean = false

    override fun isCancelled(): Boolean = this.cancelled

    override fun setCancelled(cancel: Boolean) {
        this.cancelled = cancel
    }

    override fun getClaim(): Claim = this.claim

    override fun getCause(): Cause = this.cause
}