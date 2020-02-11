package arvenwood.towns.plugin.command.element

import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.resident.ResidentService
import org.spongepowered.api.command.args.CommandElement
import org.spongepowered.api.command.args.GenericArguments
import org.spongepowered.api.text.Text

fun resident(key: Text): CommandElement =
    GenericArguments.choices(
        key,
        { ResidentService.get().residents.map(Resident::getName) },
        { ResidentService.get().getResident(it) }
    )