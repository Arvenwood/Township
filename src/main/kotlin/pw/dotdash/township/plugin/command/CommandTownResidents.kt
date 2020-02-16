package pw.dotdash.township.plugin.command

import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.service.pagination.PaginationList
import org.spongepowered.api.text.Text
import pw.dotdash.director.core.HCons
import pw.dotdash.director.core.HNil
import pw.dotdash.township.api.resident.Resident
import pw.dotdash.township.api.town.Town
import pw.dotdash.township.plugin.resident.getPlayerOrSystemResident
import pw.dotdash.township.plugin.util.ampersand
import pw.dotdash.township.plugin.util.unwrap

object CommandTownResidents {

    fun residents(src: CommandSource, args: HCons<Filter, HCons<Town?, HNil>>): CommandResult {
        val town: Town = args.tail.head ?: src.getPlayerOrSystemResident().town.unwrap()
            ?: throw CommandException(Text.of("You must specify the town argument."))

        val residents: Collection<Resident> = town.residents.filter(args.head.filter)

        val pagination: PaginationList = PaginationList.builder()
            .title("&a${residents.size} &2Residents &2(&a${town.name}&2)".ampersand())
            .padding("&6-".ampersand())
            .contents(residents.map { Text.of(it.name) })
            .build()

        pagination.sendTo(src)

        return CommandResult.success()
    }

    @Suppress("unused")
    enum class Filter(val filter: (Resident) -> Boolean) {
        /**
         * Accepts all residents.
         */
        NONE({ true }),
        /**
         * Filters for only residents that are currently online.
         */
        ONLINE({ it.player.filter { player -> !player.getOrElse(Keys.VANISH, false)}.isPresent })
    }
}