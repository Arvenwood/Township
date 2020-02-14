package pw.dotdash.township.plugin.command.element

import pw.dotdash.township.api.town.Town
import pw.dotdash.township.api.town.TownService
import org.spongepowered.api.command.args.CommandElement
import org.spongepowered.api.command.args.GenericArguments
import org.spongepowered.api.text.Text
import pw.dotdash.township.plugin.util.unwrap

fun town(key: Text): CommandElement =
    GenericArguments.choices(
        key,
        { TownService.getInstance().towns.map(Town::getName) },
        { TownService.getInstance().getTown(it).unwrap() }
    )