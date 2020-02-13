package arvenwood.towns.plugin.command

import arvenwood.towns.api.invite.Invite
import arvenwood.towns.api.invite.InviteService
import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.town.Town
import arvenwood.towns.plugin.command.element.*
import arvenwood.towns.plugin.resident.getPlayerOrSystemResident
import arvenwood.towns.plugin.util.ampersand
import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.spec.CommandExecutor
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.text.Text

object CommandTownInvite : CommandExecutor {

    val SPEC: CommandSpec = CommandSpec.builder()
        .permission("arven.towns.town.invite.base")
        .arguments(
            resident(Text.of("receiver")),
            town(Text.of("town")).requiringPermission("arven.towns.town.invite.other").optional()
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