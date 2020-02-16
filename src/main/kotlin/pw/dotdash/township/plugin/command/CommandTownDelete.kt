package pw.dotdash.township.plugin.command

import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import pw.dotdash.director.core.HCons
import pw.dotdash.director.core.HNil
import pw.dotdash.director.core.component1
import pw.dotdash.township.api.resident.Resident
import pw.dotdash.township.api.town.Town
import pw.dotdash.township.api.town.TownService
import pw.dotdash.township.plugin.resident.getPlayerOrSystemResident
import pw.dotdash.township.plugin.util.unwrap

object CommandTownDelete {

    fun delete(src: CommandSource, args: HCons<Town?, HNil>): CommandResult {
        val resident: Resident = src.getPlayerOrSystemResident()

        val (otherTown: Town?) = args

        if (otherTown != null) {
            disband(otherTown, resident)
        } else {
            val town: Town = resident.town.unwrap()
                ?: throw CommandException(Text.of("You must be in a town to use that command."))

            if (!resident.isOwner) {
                throw CommandException(Text.of("Only the owner of the town can delete it."))
            }

            disband(town, resident)
        }

        return CommandResult.success()
    }

    private fun disband(town: Town, source: Resident) {
        val residents: Collection<Resident> = town.residents
        if (TownService.getInstance().unregister(town)) {
            for (townResident: Resident in residents) {
                val townPlayer: Player = townResident.player.unwrap() ?: continue

                townPlayer.sendMessage(Text.of("Your town has been disbanded."))
            }
        }
    }
}