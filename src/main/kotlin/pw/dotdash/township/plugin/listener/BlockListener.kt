package pw.dotdash.township.plugin.listener

import pw.dotdash.township.api.claim.Claim
import pw.dotdash.township.api.claim.ClaimService
import pw.dotdash.township.api.resident.Resident
import pw.dotdash.township.api.resident.ResidentService
import org.spongepowered.api.block.BlockSnapshot
import org.spongepowered.api.data.Transaction
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.block.ChangeBlockEvent
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent
import org.spongepowered.api.event.filter.cause.Root
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors.RED
import org.spongepowered.api.util.Tristate
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import pw.dotdash.township.api.permission.ClaimPermission
import pw.dotdash.township.api.permission.ClaimPermissions
import pw.dotdash.township.plugin.util.unwrap

class BlockListener {

    @Listener
    fun onBlockBreak(event: ChangeBlockEvent.Break, @Root player: Player) {
        checkProtection(event, player, ClaimPermissions.BREAK)
    }

    @Listener
    fun onBlockPlace(event: ChangeBlockEvent.Place, @Root player: Player) {
        checkProtection(event, player, ClaimPermissions.PLACE)
    }

    @Listener
    fun onBlockModify(event: ChangeBlockEvent.Modify, @Root player: Player) {
        checkProtection(event, player, ClaimPermissions.MODIFY)
    }

    private fun checkProtection(event: ChangeBlockEvent, player: Player, permission: ClaimPermission) {
        val resident: Resident = ResidentService.getInstance().getOrCreateResident(player)

        for (transaction: Transaction<BlockSnapshot> in event.transactions) {
            val location: Location<World> = transaction.original.location.unwrap() ?: continue
            val claim: Claim = ClaimService.getInstance().getClaimAt(location).unwrap() ?: continue

            if (!resident.hasPermission(permission, claim)) {
                event.isCancelled = true
                player.sendMessage(Text.of(RED, "You do not have the permission $permission in ${claim.town.name}'s claim."))
                return
            }
        }
    }

    @Listener
    fun onChangeSign(event: ChangeSignEvent, @Root player: Player) {
        val resident: Resident = ResidentService.getInstance().getOrCreateResident(player)
        val claim: Claim = ClaimService.getInstance().getClaimAt(event.targetTile.location).unwrap() ?: return

        if (!resident.hasPermission(ClaimPermissions.MODIFY, claim)) {
            event.isCancelled = true
            player.sendMessage(Text.of(RED, "That chunk is owned by the town ${claim.town.name}."))
            return
        }
    }
}