package arvenwood.towns.plugin.command.element

import arvenwood.towns.api.town.Town
import arvenwood.towns.api.town.TownService
import org.spongepowered.api.command.args.CommandElement
import org.spongepowered.api.command.args.GenericArguments
import org.spongepowered.api.text.Text

fun town(key: Text): CommandElement =
    GenericArguments.choices(
        key,
        { TownService.getInstance().towns.map(Town::getName) },
        { TownService.getInstance().getTown(it) }
    )