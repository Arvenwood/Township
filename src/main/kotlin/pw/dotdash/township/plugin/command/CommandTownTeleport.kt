package pw.dotdash.township.plugin.command

import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.spec.CommandExecutor
import org.spongepowered.api.command.spec.CommandSpec

object CommandTownTeleport : CommandExecutor {

    val SPEC: CommandSpec = CommandSpec.builder()
        .permission("township.town.teleport.base")
        .executor(this)
        .build()

    override fun execute(src: CommandSource, args: CommandContext): CommandResult {
        TODO("not implemented")
    }
}