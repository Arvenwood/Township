package pw.dotdash.township.plugin.command.element

import org.spongepowered.api.command.args.CommandElement
import org.spongepowered.api.command.args.GenericArguments

fun CommandElement.optional(): CommandElement =
    GenericArguments.optional(this)

fun CommandElement.optional(default: Any): CommandElement =
    GenericArguments.optional(this, default)

fun CommandElement.requiringPermission(permission: String): CommandElement =
    GenericArguments.requiringPermission(this, permission)

fun CommandElement.onlyOne(): CommandElement =
    GenericArguments.onlyOne(this)