package pw.dotdash.township.plugin.command

import pw.dotdash.township.api.resident.Resident
import pw.dotdash.township.api.resident.ResidentService
import pw.dotdash.township.api.town.Town
import pw.dotdash.township.plugin.command.element.*
import pw.dotdash.township.plugin.util.ampersand
import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.args.GenericArguments.player
import org.spongepowered.api.command.spec.CommandExecutor
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

object CommandTownJoin : CommandExecutor {

    val SPEC: CommandSpec = CommandSpec.builder()
        .permission("township.town.join.base")
        .arguments(
            town(Text.of("town")),
            player(Text.of("player")).onlyOne().requiringPermission("township.town.join.other").optional()
        )
        .executor(this)
        .build()

    override fun execute(src: CommandSource, args: CommandContext): CommandResult {
        val town: Town = args.requireOne("town")

        val player: Player = args.maybeOne("player")
            ?: src as? Player
            ?: throw CommandException(Text.of("You must specify the player argument."))

        if (!town.isOpen) {
            throw CommandException(Text.of("You need an invite to join ${town.name}"))
        }

        val resident: Resident = ResidentService.getInstance().getOrCreateResident(player)

        town.addResident(resident)

        town.sendMessage("&f${resident.name}&b has joined the town.".ampersand())

        return CommandResult.success()
    }

}