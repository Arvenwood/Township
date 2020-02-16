package pw.dotdash.township.plugin.command

import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.text.Text
import pw.dotdash.director.core.HCons
import pw.dotdash.director.core.HNil
import pw.dotdash.township.api.invite.Invite
import pw.dotdash.township.api.invite.InviteService
import pw.dotdash.township.api.resident.Resident
import pw.dotdash.township.api.town.Town
import pw.dotdash.township.plugin.resident.getPlayerOrSystemResident
import pw.dotdash.township.plugin.util.ampersand
import pw.dotdash.township.plugin.util.unwrap

object CommandTownInvite {

    fun invite(src: CommandSource, args: HCons<Town?, HCons<Resident, HNil>>): CommandResult {
        val sender: Resident = src.getPlayerOrSystemResident()

        val receiver: Resident = args.tail.head
        val town: Town = args.head ?: sender.town.unwrap()
            ?: throw CommandException(Text.of("You must be in a town to use this command."))

        val invite: Invite = Invite.builder()
            .sender(sender)
            .receiver(receiver)
            .town(town)
            .build()

        InviteService.getInstance().register(invite)

        town.sendMessage("&f${receiver.name}&b has been invited to the town by &f${sender.name}&b.".ampersand())

        return CommandResult.success()
    }
}