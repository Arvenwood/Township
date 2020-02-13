package arvenwood.towns.plugin.command

import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.town.Town
import arvenwood.towns.api.town.TownService
import arvenwood.towns.plugin.resident.getPlayerOrSystemResident
import org.spongepowered.api.Sponge
import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.args.GenericArguments.string
import org.spongepowered.api.command.spec.CommandExecutor
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.text.Text

object CommandTownCreate : CommandExecutor {

    val SPEC: CommandSpec = CommandSpec.builder()
        .permission("arven.towns.town.create.base")
        .arguments(string(Text.of("name")))
        .executor(this)
        .build()

    private val nameRegex = Regex("^[a-zA-Z0-9]{2,32}$")

    override fun execute(src: CommandSource, args: CommandContext): CommandResult {
        val resident: Resident = src.getPlayerOrSystemResident()

        if (resident.town.isPresent) {
            throw CommandException(Text.of("You must leave your current town to create a new one."))
        }

        val name: String = args.requireOne("name")

        if (!name.matches(this.nameRegex)) {
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