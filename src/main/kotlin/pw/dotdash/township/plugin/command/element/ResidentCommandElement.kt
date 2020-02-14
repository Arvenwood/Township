package pw.dotdash.township.plugin.command.element

import pw.dotdash.township.api.resident.Resident
import pw.dotdash.township.api.resident.ResidentService
import org.spongepowered.api.command.args.CommandElement
import org.spongepowered.api.command.args.GenericArguments
import org.spongepowered.api.text.Text
import pw.dotdash.township.plugin.util.unwrap

fun resident(key: Text): CommandElement =
    GenericArguments.choices(
        key,
        { ResidentService.getInstance().residents.map(Resident::getName) },
        { ResidentService.getInstance().getResident(it).unwrap() }
    )