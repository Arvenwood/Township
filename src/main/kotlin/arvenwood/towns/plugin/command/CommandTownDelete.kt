package arvenwood.towns.plugin.command

import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.town.Town
import arvenwood.towns.api.town.TownService
import arvenwood.towns.plugin.command.element.*
import arvenwood.towns.plugin.resident.getPlayerOrSystemResident
import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.spec.CommandExecutor
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

object CommandTownDelete : CommandExecutor {

    val SPEC: CommandSpec = CommandSpec.builder()
        .permission("arven.towns.town.delete.base")
        .arguments(
            town(Text.of("town")).requiringPermission("arven.towns.town.delete.other").optional()
        )
        .executor(this)
        .build()

    override fun execute(src: CommandSource, args: CommandContext): CommandResult {
        val resident: Resident = src.getPlayerOrSystemResident()

        val otherTown: Town? = args.maybeOne("town")

        if (otherTown != null) {
            disband(otherTown, resident)
        } else {
            val town: Town = args.maybeOne("town")
                ?: resident.town.orElse(null)
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
                val townPlayer: Player = townResident.player.orElse(null) ?: continue

                townPlayer.sendMessage(Text.of("Your town has been disbanded."))
            }
        }
    }
}