package pw.dotdash.township.plugin.command.parameter

import pw.dotdash.director.core.HList
import pw.dotdash.director.core.parameter.choices
import pw.dotdash.director.core.value.ValueParameter
import pw.dotdash.township.api.town.Town
import pw.dotdash.township.api.town.TownService
import pw.dotdash.township.plugin.util.unwrap

fun town(): ValueParameter<Any?, HList<*>, Town> =
    choices(
        { TownService.getInstance().towns.map(Town::getName) },
        { TownService.getInstance().getTown(it).unwrap() }
    )