package arvenwood.towns.plugin.command

import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.resident.ResidentService
import arvenwood.towns.api.town.Town
import arvenwood.towns.api.town.TownService
import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.args.GenericArguments.*
import org.spongepowered.api.command.spec.CommandExecutor
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

object CommandTownDelete : CommandExecutor {

    val SPEC: CommandSpec = CommandSpec.builder()
        .permission("arven.towns.town.delete.base")
        .arguments(optional(requiringPermission(onlyOne(player(Text.of("player"))), "arven.towns.town.delete.other")))
        .executor(this)
        .build()

    override fun execute(src: CommandSource, args: CommandContext): CommandResult {
        val player: Player = args.getOne<Player>("player").orElse(null)
            ?: src as? Player
            ?: throw CommandException(Text.of("You must specify a player argument or be one to run this command!"))

        val resident: Resident = ResidentService.get().getOrCreateResident(player)

        val town: Town = resident.town.orElse(null)
            ?: throw CommandException(Text.of("You must be in a town to use that command."))

        if (!resident.isOwner) {
            throw CommandException(Text.of("Only the owner of the town can delete it."))
        }

        val residents: Collection<Resident> = town.residents
        if (TownService.get().unregister(town)) {
            for (townResident: Resident in residents) {
                val townPlayer: Player = resident.player.orElse(null) ?: continue

                townPlayer.sendMessage(Text.of("Your town has been disbanded."))
            }
        }

        return CommandResult.success()
    }
}