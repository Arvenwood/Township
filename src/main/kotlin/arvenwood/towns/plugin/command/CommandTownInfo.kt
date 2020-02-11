package arvenwood.towns.plugin.command

import arvenwood.towns.api.resident.ResidentService
import arvenwood.towns.api.town.Town
import arvenwood.towns.plugin.command.element.town
import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.args.GenericArguments.optional
import org.spongepowered.api.command.spec.CommandExecutor
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.service.pagination.PaginationList
import org.spongepowered.api.text.Text

object CommandTownInfo : CommandExecutor {

    val SPEC: CommandSpec = CommandSpec.builder()
        .permission("arven.towns.town.info.base")
        .arguments(optional(town(Text.of("town"))))
        .executor(this)
        .build()

    override fun execute(src: CommandSource, args: CommandContext): CommandResult {
        val town: Town = args.getOne<Town>("town").orElse(null)
            ?: (src as? Player)?.let { ResidentService.get().getOrCreateResident(src).town.orElse(null) }
            ?: throw CommandException(Text.of("You must specify the town argument."))

        val pagination: PaginationList = PaginationList.builder()
            .title(Text.of("Town: ${town.name}"))
            .build()

        return CommandResult.success()
    }
}