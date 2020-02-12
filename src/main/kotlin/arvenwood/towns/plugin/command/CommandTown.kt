package arvenwood.towns.plugin.command

import org.spongepowered.api.command.spec.CommandSpec

object CommandTown {

    val SPEC: CommandSpec = CommandSpec.builder()
        .permission("arven.towns.town.base")
        .child(CommandTownClaim.SPEC, "claim", "cl")
        .child(CommandTownCreate.SPEC, "create")
        .child(CommandTownDelete.SPEC, "delete")
        .child(CommandTownHere.SPEC, "here")
        .child(CommandTownInfo.SPEC, "info", "i")
        .child(CommandTownJoin.SPEC, "join")
        .child(CommandTownLeave.SPEC, "leave")
        .child(CommandTownList.SPEC, "list")
        .child(CommandTownResidents.SPEC, "residents", "res")
        .child(CommandTownUnclaim.SPEC, "unclaim")
        .build()
}