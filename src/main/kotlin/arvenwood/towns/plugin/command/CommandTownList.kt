package arvenwood.towns.plugin.command

import arvenwood.towns.api.town.TownService
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.spec.CommandExecutor
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.service.pagination.PaginationList
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors.GOLD
import org.spongepowered.api.text.format.TextColors.YELLOW

object CommandTownList : CommandExecutor {

    val SPEC: CommandSpec = CommandSpec.builder()
        .permission("arven.towns.town.list.base")
        .executor(this)
        .build()

    override fun execute(src: CommandSource, args: CommandContext): CommandResult {
        val pagination: PaginationList = PaginationList.builder()
            .title(Text.of(YELLOW, "Towns"))
            .padding(Text.of(GOLD, "="))
            .contents(TownService.get().towns.map { Text.of(it.name, " (", it.residents.size, ")") })
            .build()

        pagination.sendTo(src)

        return CommandResult.success()
    }
}