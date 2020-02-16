package pw.dotdash.township.plugin.command

import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import pw.dotdash.director.core.HCons
import pw.dotdash.director.core.HNil
import pw.dotdash.township.api.claim.Claim
import pw.dotdash.township.api.claim.ClaimService
import pw.dotdash.township.plugin.util.unwrap

object CommandTownHere {

    fun here(src: CommandSource, args: HCons<Player?, HNil>): CommandResult {
        val player: Player = args.head ?: src as? Player
            ?: throw CommandException(Text.of("You must specify the player argument."))

        val claim: Claim = ClaimService.getInstance().getClaimAt(player.location).unwrap()
            ?: throw CommandException(Text.of("No town has claimed this chunk."))

        CommandTownInfo.showTown(claim.town).sendTo(src)

        return CommandResult.success()
    }
}