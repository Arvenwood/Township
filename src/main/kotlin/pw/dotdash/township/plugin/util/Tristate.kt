package pw.dotdash.township.plugin.util

import org.spongepowered.api.util.Tristate

fun Boolean?.toTristate(): Tristate = when (this) {
    true -> Tristate.TRUE
    false -> Tristate.FALSE
    null -> Tristate.UNDEFINED
}

inline fun Tristate.or(other: () -> Tristate): Tristate =
    if (this != Tristate.UNDEFINED) {
        this
    } else {
        other()
    }