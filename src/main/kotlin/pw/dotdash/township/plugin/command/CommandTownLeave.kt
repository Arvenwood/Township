package pw.dotdash.township.plugin.command

import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors.YELLOW
import pw.dotdash.director.core.HCons
import pw.dotdash.director.core.HNil
import pw.dotdash.township.api.resident.Resident
import pw.dotdash.township.api.resident.ResidentService
import pw.dotdash.township.api.town.Town
import pw.dotdash.township.plugin.util.unwrap

object CommandTownLeave {

    fun leave(src: CommandSource, args: HCons<Player?, HNil>): CommandResult {
        val player: Player = args.head ?: src as? Player
            ?: throw CommandException(Text.of("You must specify the player argument."))

        val resident: Resident = ResidentService.getInstance().getOrCreateResident(player)

        val town: Town = resident.town.unwrap()
            ?: throw CommandException(Text.of("You must be in a town to use that command."))

        if (resident.isOwner) {
            throw CommandException(Text.of("The owner cannot leave the town. Instead, use ", YELLOW, "/town delete"))
        }

        town.removeResident(resident)

        return CommandResult.success()
    }
}