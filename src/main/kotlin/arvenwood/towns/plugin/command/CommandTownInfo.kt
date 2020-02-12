package arvenwood.towns.plugin.command

import arvenwood.towns.api.town.Town
import arvenwood.towns.plugin.command.element.maybeOne
import arvenwood.towns.plugin.command.element.optional
import arvenwood.towns.plugin.command.element.town
import arvenwood.towns.plugin.resident.getPlayerOrSystemResident
import arvenwood.towns.plugin.util.text
import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.spec.CommandExecutor
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.service.pagination.PaginationList
import org.spongepowered.api.text.Text

object CommandTownInfo : CommandExecutor {

    val SPEC: CommandSpec = CommandSpec.builder()
        .permission("arven.towns.town.info.base")
        .arguments(town(Text.of("town")).optional())
        .executor(this)
        .build()

    override fun execute(src: CommandSource, args: CommandContext): CommandResult {
        val town: Town = args.maybeOne("town")
            ?: src.getPlayerOrSystemResident().town.orElse(null)
            ?: throw CommandException(Text.of("You must specify the town argument."))

        val pagination: PaginationList = showTown(town)

        pagination.sendTo(src)

        return CommandResult.success()
    }

    internal fun showTown(town: Town): PaginationList {
        return PaginationList.builder()
            .title("&2Town: &f${town.name}".text())
            .padding("&6-".text())
            .contents(
                "&2Open: ${if (town.isOpen) "&aYes" else "&cNo"}".text(),
                "&2Town Size: &a${town.claims.size}".text(),
                "&2Owner: &a${town.owner.name}".text(),
                "&2Residents &a[${town.residents.size}]&2: &f${town.residents.joinToString(limit = 15) { it.name }}".text()
            )
            .build()
    }
}