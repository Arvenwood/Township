package arvenwood.towns.plugin.command

import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.resident.ResidentService
import arvenwood.towns.api.town.Town
import arvenwood.towns.plugin.command.element.maybeOne
import arvenwood.towns.plugin.command.element.optional
import arvenwood.towns.plugin.command.element.town
import arvenwood.towns.plugin.util.text
import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.args.GenericArguments.enumValue
import org.spongepowered.api.command.spec.CommandExecutor
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.service.pagination.PaginationList
import org.spongepowered.api.text.Text

object CommandTownResidents : CommandExecutor {

    val SPEC: CommandSpec = CommandSpec.builder()
        .permission("arven.towns.town.residents.base")
        .arguments(
            town(Text.of("town")).optional(),
            enumValue(Text.of("filter"), Filter::class.java).optional(Filter.NONE)
        )
        .executor(this)
        .build()

    override fun execute(src: CommandSource, args: CommandContext): CommandResult {
        val town: Town = args.maybeOne("town")
            ?: (src as? Player)?.let { ResidentService.get().getOrCreateResident(it).town.orElse(null) }
            ?: throw CommandException(Text.of("You must specify the town argument."))

        val filter: Filter = args.requireOne("filter")
        val residents: Collection<Resident> = town.residents.filter(filter.filter)

        val pagination: PaginationList = PaginationList.builder()
            .title("&a${residents.size} &2Residents &2(&a${town.name}&2)".text())
            .padding("&6-".text())
            .contents(residents.map { Text.of(it.name) })
            .build()

        pagination.sendTo(src)

        return CommandResult.success()
    }

    @Suppress("unused")
    private enum class Filter(val filter: (Resident) -> Boolean) {
        /**
         * Accepts all residents.
         */
        NONE({ true }),
        /**
         * Filters for only residents that are currently online.
         */
        ONLINE({ it.player.isPresent })
    }
}