package pw.dotdash.township.plugin.command.element

import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.args.CommandElement
import org.spongepowered.api.command.args.GenericArguments
import org.spongepowered.api.text.Text

fun <T> CommandContext.maybeOne(key: String): T? =
    this.getOne<T>(key).orElse(null)

fun <T> CommandContext.maybeOne(key: Text): T? =
    this.getOne<T>(key).orElse(null)

fun CommandElement.optional(): CommandElement =
    GenericArguments.optional(this)

fun CommandElement.optional(default: Any): CommandElement =
    GenericArguments.optional(this, default)

fun CommandElement.requiringPermission(permission: String): CommandElement =
    GenericArguments.requiringPermission(this, permission)

fun CommandElement.onlyOne(): CommandElement =
    GenericArguments.onlyOne(this)