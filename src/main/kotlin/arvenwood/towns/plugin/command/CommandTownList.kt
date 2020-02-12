package arvenwood.towns.plugin.command

import arvenwood.towns.api.town.Town
import arvenwood.towns.api.town.TownService
import arvenwood.towns.plugin.command.element.optional
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.args.GenericArguments.enumValue
import org.spongepowered.api.command.spec.CommandExecutor
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.service.pagination.PaginationList
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors.GOLD
import org.spongepowered.api.text.format.TextColors.YELLOW

object CommandTownList : CommandExecutor {

    val SPEC: CommandSpec = CommandSpec.builder()
        .permission("arven.towns.town.list.base")
        .arguments(enumValue(Text.of("sortBy"), SortBy::class.java).optional(SortBy.NAME))
        .executor(this)
        .build()

    override fun execute(src: CommandSource, args: CommandContext): CommandResult {
        val sortBy: SortBy = args.requireOne("sortBy")
        val towns: Collection<Town> = TownService.get().towns.sortedWith(sortBy.comparator)

        val pagination: PaginationList = PaginationList.builder()
            .title(Text.of(YELLOW, "Towns"))
            .padding(Text.of(GOLD, "-"))
            .contents(towns.map { Text.of(it.name, " (", it.residents.size, ")") })
            .build()

        pagination.sendTo(src)

        return CommandResult.success()
    }

    @Suppress("unused")
    private enum class SortBy(val comparator: Comparator<Town>) {
        /**
         * Sort alphabetically.
         */
        NAME(compareBy(Town::getName)),
        /**
         * Sort by the number of residents.
         */
        RESIDENT_SIZE(compareBy { it.residents.size }),
        /**
         * Sort by the number of residents currently online.
         */
        RESIDENT_ONLINE_SIZE(compareBy { it.residents.filter { resident -> resident.player.isPresent }.size });
    }
}