package arvenwood.towns.plugin.listener

import arvenwood.towns.api.claim.Claim
import arvenwood.towns.api.claim.ClaimService
import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.resident.ResidentService
import org.spongepowered.api.block.BlockSnapshot
import org.spongepowered.api.data.Transaction
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.block.ChangeBlockEvent
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent
import org.spongepowered.api.event.filter.cause.Root
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors.RED
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World

class BlockListener {

    @Listener
    fun onBlockBreak(event: ChangeBlockEvent.Break, @Root player: Player) {
        checkProtection(event, player)
    }

    @Listener
    fun onBlockPlace(event: ChangeBlockEvent.Place, @Root player: Player) {
        checkProtection(event, player)
    }

    private fun checkProtection(event: ChangeBlockEvent, player: Player) {
        val resident: Resident = ResidentService.get().getResident(player.uniqueId).orElse(null) ?: return

        for (transaction: Transaction<BlockSnapshot> in event.transactions) {
            val location: Location<World> = transaction.original.location.orElse(null) ?: continue
            val claim: Claim = ClaimService.get().getClaimAt(location).orElse(null) ?: continue

            if (claim.town != resident.town) {
                event.isCancelled = true
                player.sendMessage(Text.of(RED, "That chunk is owned by the town ${claim.town.name}."))
                return
            }
        }
    }

    @Listener
    fun onChangeSign(event: ChangeSignEvent, @Root player: Player) {
        val resident: Resident = ResidentService.get().getResident(player.uniqueId).orElse(null) ?: return
        val claim: Claim = ClaimService.get().getClaimAt(event.targetTile.location).orElse(null) ?: return

        if (claim.town != resident.town) {
            event.isCancelled = true
            player.sendMessage(Text.of(RED, "That chunk is owned by the town ${claim.town.name}."))
            return
        }
    }
}