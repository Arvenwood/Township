package pw.dotdash.township.plugin.event.town

import pw.dotdash.township.api.event.town.DeleteTownEvent
import pw.dotdash.township.api.town.Town
import org.spongepowered.api.event.cause.Cause

data class DeleteTownEventImpl(
    private val town: Town,
    private val cause: Cause
) : DeleteTownEvent {

    private var cancelled: Boolean = false

    override fun isCancelled(): Boolean = this.cancelled

    override fun setCancelled(cancel: Boolean) {
        this.cancelled = cancel
    }

    override fun getTown(): Town = this.town

    override fun getCause(): Cause = this.cause
}