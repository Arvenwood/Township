package pw.dotdash.township.plugin.util

import org.spongepowered.api.event.Cancellable
import org.spongepowered.api.event.Event
import org.spongepowered.api.event.EventManager

fun <T> EventManager.tryPost(event: T): T? where T : Event, T : Cancellable {
    this.post(event)
    return event.takeUnless { it.isCancelled }
}