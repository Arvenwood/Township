package pw.dotdash.township.plugin.command.parameter

import org.spongepowered.api.command.CommandSource
import pw.dotdash.director.core.HList
import pw.dotdash.director.core.Parameter
import pw.dotdash.director.core.lexer.CommandTokens
import pw.dotdash.director.core.parameter.choices
import pw.dotdash.director.core.value.ValueParameter
import pw.dotdash.township.api.resident.Resident
import pw.dotdash.township.api.resident.ResidentService
import pw.dotdash.township.plugin.resident.getPlayerOrSystemResident
import pw.dotdash.township.plugin.util.unwrap

fun resident(): ValueParameter<Any?, HList<*>, Resident> =
    choices(
        { ResidentService.getInstance().residents.map(Resident::getName) },
        { ResidentService.getInstance().getResident(it).unwrap() }
    )

fun sourceResident(): Parameter<CommandSource, HList<*>, Resident> = SourceResidentParameter.key("srcResident")

object SourceResidentParameter : ValueParameter<CommandSource, HList<*>, Resident> {
    override fun parse(source: CommandSource, tokens: CommandTokens, previous: HList<*>): Resident =
        source.getPlayerOrSystemResident()

    override fun getUsage(source: CommandSource, key: String): String = ""
}