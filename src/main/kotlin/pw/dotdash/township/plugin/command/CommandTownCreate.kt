package pw.dotdash.township.plugin.command

import org.spongepowered.api.Sponge
import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.text.Text
import pw.dotdash.director.core.HCons
import pw.dotdash.director.core.HNil
import pw.dotdash.director.core.component1
import pw.dotdash.township.api.resident.Resident
import pw.dotdash.township.api.town.Town
import pw.dotdash.township.api.town.TownService
import pw.dotdash.township.plugin.resident.getPlayerOrSystemResident

object CommandTownCreate {

    private val nameRegex = Regex("^[a-zA-Z0-9]{2,32}$")

    fun create(src: CommandSource, args: HCons<String, HNil>): CommandResult {
        val resident: Resident = src.getPlayerOrSystemResident()

        if (resident.town.isPresent) {
            throw CommandException(Text.of("You must leave your current town to create a new one."))
        }

        val (name: String) = args

        if (!name.matches(nameRegex)) {
            throw CommandException(Text.of("Town name must be alphanumeric and from 2 to 32 characters."))
        }

        if (TownService.getInstance().getTown(name).isPresent) {
            throw CommandException(Text.of("The town '$name' already exists!"))
        }

        val town: Town = Town.builder()
            .name(name)
            .owner(resident)
            .build()

        if (TownService.getInstance().register(town)) {
            Sponge.getServer().broadcastChannel.send(Text.of("The town of ${town.name} has been founded by ${src.name}."))
        } else {
            throw CommandException(Text.of("Failed to create the town. Please retry."))
        }

        return CommandResult.success()
    }
}