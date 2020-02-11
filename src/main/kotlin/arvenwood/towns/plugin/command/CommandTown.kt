package arvenwood.towns.plugin.command

import org.spongepowered.api.command.spec.CommandSpec

object CommandTown {

    val SPEC: CommandSpec = CommandSpec.builder()
        .permission("arven.towns.town.base")
        .child(CommandTownClaim.SPEC, "claim")
        .child(CommandTownCreate.SPEC, "create")
        .child(CommandTownDelete.SPEC, "delete")
        .child(CommandTownList.SPEC, "list")
        .child(CommandTownUnclaim.SPEC, "unclaim")
        .build()
}