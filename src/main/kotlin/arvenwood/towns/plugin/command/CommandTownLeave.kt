package arvenwood.towns.plugin.command

import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.resident.ResidentService
import arvenwood.towns.api.town.Town
import arvenwood.towns.plugin.command.element.maybeOne
import arvenwood.towns.plugin.command.element.onlyOne
import arvenwood.towns.plugin.command.element.optional
import arvenwood.towns.plugin.command.element.requiringPermission
import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.args.GenericArguments.player
import org.spongepowered.api.command.spec.CommandExecutor
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors.YELLOW

object CommandTownLeave : CommandExecutor {

    val SPEC: CommandSpec = CommandSpec.builder()
        .permission("arven.towns.town.leave.base")
        .arguments(
            player(Text.of("player")).onlyOne().requiringPermission("arven.towns.town.leave.other").optional()
        )
        .executor(this)
        .build()

    override fun execute(src: CommandSource, args: CommandContext): CommandResult {
        val player: Player = args.maybeOne("player")
            ?: src as? Player
            ?: throw CommandException(Text.of("You must specify the player argument."))

        val resident: Resident = ResidentService.getInstance().getOrCreateResident(player)

        val town: Town = resident.town.orElse(null)
            ?: throw CommandException(Text.of("You must be in a town to use that command."))

        if (resident.isOwner) {
            throw CommandException(Text.of("The owner cannot leave the town. Instead, use ", YELLOW, "/town delete"))
        }

        town.removeResident(resident)

        return CommandResult.success()
    }
}