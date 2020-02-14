package pw.dotdash.township.plugin.command

import pw.dotdash.township.api.town.Town
import pw.dotdash.township.plugin.command.element.maybeOne
import pw.dotdash.township.plugin.command.element.optional
import pw.dotdash.township.plugin.command.element.town
import pw.dotdash.township.plugin.resident.getPlayerOrSystemResident
import pw.dotdash.township.plugin.util.ampersand
import org.spongepowered.api.Sponge
import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.spec.CommandExecutor
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.service.economy.Currency
import org.spongepowered.api.service.economy.EconomyService
import org.spongepowered.api.service.pagination.PaginationList
import org.spongepowered.api.text.Text

object CommandTownInfo : CommandExecutor {

    val SPEC: CommandSpec = CommandSpec.builder()
        .permission("township.town.info.base")
        .arguments(town(Text.of("town")).optional())
        .executor(this)
        .build()

    override fun execute(src: CommandSource, args: CommandContext): CommandResult {
        val town: Town = args.maybeOne<Town>("town")
            ?: src.getPlayerOrSystemResident().town.orElse(null)
            ?: throw CommandException(Text.of("You must specify the town argument."))

        val pagination: PaginationList = showTown(town)

        pagination.sendTo(src)

        return CommandResult.success()
    }

    internal fun showTown(town: Town): PaginationList {
        return PaginationList.builder()
            .title("&2Town: &f${town.name}".ampersand())
            .padding("&6-".ampersand())
            .contents(
                "&2Open: ${if (town.isOpen) "&aYes" else "&cNo"}".ampersand(),
                "&2Town Size: &a${town.claims.size}".ampersand(),
                Text.of("&2Balance: &a".ampersand(), defaultCurrency?.format(town.balance) ?: Text.of("$0")),
                "&2Owner: &a${town.owner.name}".ampersand(),
                "&2Residents &a[${town.residents.size}]&2: &f${town.residents.joinToString(limit = 15) { it.name }}".ampersand()
            )
            .build()
    }

    private val defaultCurrency: Currency?
        get() = Sponge.getServiceManager().provide(EconomyService::class.java).orElse(null)?.defaultCurrency
}