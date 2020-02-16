package pw.dotdash.township.plugin.command

import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.service.pagination.PaginationList
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors.GOLD
import org.spongepowered.api.text.format.TextColors.YELLOW
import pw.dotdash.director.core.HCons
import pw.dotdash.director.core.HNil
import pw.dotdash.township.api.town.Town
import pw.dotdash.township.api.town.TownService

object CommandTownList {

    fun list(src: CommandSource, args: HCons<SortBy, HNil>): CommandResult {
        val towns: Collection<Town> = TownService.getInstance().towns.sortedWith(args.head.comparator)

        val pagination: PaginationList = PaginationList.builder()
            .title(Text.of(YELLOW, "Towns"))
            .padding(Text.of(GOLD, "-"))
            .contents(towns.map { Text.of(it.name, " (", it.residents.size, ")") })
            .build()

        pagination.sendTo(src)

        return CommandResult.success()
    }

    @Suppress("unused")
    enum class SortBy(val comparator: Comparator<Town>) {
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