package arvenwood.towns.plugin.command

import arvenwood.towns.api.claim.Claim
import arvenwood.towns.api.claim.ClaimService
import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.resident.ResidentService
import arvenwood.towns.api.town.Town
import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.spec.CommandExecutor
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors.GREEN

object CommandTownUnclaim : CommandExecutor {

    val SPEC: CommandSpec = CommandSpec.builder()
        .permission("arven.towns.town.unclaim.base")
        .executor(this)
        .build()

    override fun execute(src: CommandSource, args: CommandContext): CommandResult {
        if (src !is Player) throw CommandException(Text.of("You must be a player to use that command."))

        val resident: Resident = ResidentService.get().getOrCreateResident(src)

        val town: Town = resident.town
            ?: throw CommandException(Text.of("You must be in a town to use that command."))

        val claim: Claim = ClaimService.get().getClaimAt(src.location)
            ?: throw CommandException(Text.of("This chunk has not been claimed."))

        if (claim.town != town) {
            throw CommandException(Text.of("Your town does not own this chunk."))
        }

        ClaimService.get().unregister(claim)
        src.sendMessage(Text.of(GREEN, "Unclaimed the chunk at ${claim.chunkPosition}"))

        return CommandResult.success()
    }
}