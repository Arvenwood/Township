package pw.dotdash.township.plugin.command

import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import pw.dotdash.director.core.HCons
import pw.dotdash.director.core.HNil
import pw.dotdash.township.api.resident.Resident
import pw.dotdash.township.api.resident.ResidentService
import pw.dotdash.township.api.town.Town
import pw.dotdash.township.plugin.util.ampersand

object CommandTownJoin {

    fun join(src: CommandSource, args: HCons<Player?, HCons<Town, HNil>>): CommandResult {
        val town: Town = args.tail.head

        val player: Player = args.head ?: src as? Player
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