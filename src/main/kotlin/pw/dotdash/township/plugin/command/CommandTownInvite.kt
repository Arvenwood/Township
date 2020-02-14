package pw.dotdash.township.plugin.command

import pw.dotdash.township.api.invite.Invite
import pw.dotdash.township.api.invite.InviteService
import pw.dotdash.township.api.resident.Resident
import pw.dotdash.township.api.town.Town
import pw.dotdash.township.plugin.command.element.*
import pw.dotdash.township.plugin.resident.getPlayerOrSystemResident
import pw.dotdash.township.plugin.util.ampersand
import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.spec.CommandExecutor
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.text.Text

object CommandTownInvite : CommandExecutor {

    val SPEC: CommandSpec = CommandSpec.builder()
        .permission("township.town.invite.base")
        .arguments(
            resident(Text.of("receiver")),
            town(Text.of("town")).requiringPermission("township.town.invite.other").optional()
        )
        .executor(this)
        .build()

    override fun execute(src: CommandSource, args: CommandContext): CommandResult {
        val sender: Resident = src.getPlayerOrSystemResident()

        val receiver: Resident = args.requireOne("receiver")
        val town: Town = args.maybeOne("town")
            ?: sender.town.orElse(null)
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