package arvenwood.towns.plugin.command

import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.resident.ResidentService
import arvenwood.towns.api.town.Town
import arvenwood.towns.plugin.command.element.*
import arvenwood.towns.plugin.util.text
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
        .permission("arven.towns.town.join.base")
        .arguments(
            town(Text.of("town")),
            player(Text.of("player")).onlyOne().requiringPermission("arven.towns.town.join.other").optional()
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

        val resident: Resident = ResidentService.get().getOrCreateResident(player)

        town.addResident(resident)

        town.sendMessage("&f${resident.name}&b has joined the town.".text())

        return CommandResult.success()
    }

}