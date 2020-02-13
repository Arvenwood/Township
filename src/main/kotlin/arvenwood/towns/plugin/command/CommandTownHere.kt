package arvenwood.towns.plugin.command

import arvenwood.towns.api.claim.Claim
import arvenwood.towns.api.claim.ClaimService
import arvenwood.towns.plugin.command.element.onlyOne
import arvenwood.towns.plugin.command.element.optional
import arvenwood.towns.plugin.command.element.requiringPermission
import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.args.GenericArguments.*
import org.spongepowered.api.command.spec.CommandExecutor
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

object CommandTownHere : CommandExecutor {

    val SPEC: CommandSpec = CommandSpec.builder()
        .permission("arven.towns.town.here.base")
        .arguments(
            player(Text.of("player")).onlyOne().requiringPermission("arven.towns.town.here.other").optional()
        )
        .executor(this)
        .build()

    override fun execute(src: CommandSource, args: CommandContext): CommandResult {
        val player: Player = args.getOne<Player>("player").orElse(null)
            ?: src as? Player
            ?: throw CommandException(Text.of("You must specify the player argument."))

        val claim: Claim = ClaimService.getInstance().getClaimAt(player.location).orElse(null)
            ?: throw CommandException(Text.of("No town has claimed this chunk."))

        CommandTownInfo.showTown(claim.town).sendTo(src)

        return CommandResult.success()
    }
}