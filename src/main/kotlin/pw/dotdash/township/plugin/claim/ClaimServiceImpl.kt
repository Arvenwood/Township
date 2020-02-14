package pw.dotdash.township.plugin.claim

import pw.dotdash.township.api.claim.Claim
import pw.dotdash.township.api.claim.ClaimService
import pw.dotdash.township.api.town.Town
import pw.dotdash.township.plugin.event.claim.CreateClaimEventImpl
import pw.dotdash.township.plugin.event.claim.DeleteClaimEventImpl
import pw.dotdash.township.plugin.storage.DataLoader
import pw.dotdash.township.plugin.storage.StorageBackedService
import pw.dotdash.township.plugin.util.tryPost
import com.flowpowered.math.vector.Vector3i
import com.google.common.collect.Table
import com.google.common.collect.Tables
import org.spongepowered.api.Sponge
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import java.util.*
import kotlin.collections.HashMap

class ClaimServiceImpl : ClaimService, StorageBackedService {

    private val claimTable: Table<World, Vector3i, Claim> =
        Tables.newCustomTable(HashMap()) { HashMap<Vector3i, Claim>() }

    override fun getClaimAt(location: Location<World>): Optional<Claim> =
        Optional.ofNullable(this.claimTable[location.extent, location.chunkPosition])

    override fun getClaimsFor(world: World): Collection<Claim> =
        this.claimTable.row(world).values.toSet()

    override fun getClaimsFor(town: Town): Collection<Claim> =
        this.claimTable.values().filter { it.town == town }

    override fun contains(claim: Claim): Boolean =
        this.claimTable.contains(claim.world, claim.chunkPosition)

    override fun register(claim: Claim): Boolean {
        if (claim.chunkPosition in this.claimTable.row(claim.world)) {
            return false
        }

        Sponge.getEventManager().tryPost(CreateClaimEventImpl(claim, Sponge.getCauseStackManager().currentCause))
            ?: return false

        this.claimTable.put(claim.world, claim.chunkPosition, claim)
        return true
    }

    override fun unregister(claim: Claim): Boolean {
        if (claim.chunkPosition !in this.claimTable.row(claim.world)) {
            return false
        }

        Sponge.getEventManager().tryPost(DeleteClaimEventImpl(claim, Sponge.getCauseStackManager().currentCause))
            ?: return false

        this.claimTable.remove(claim.world, claim.chunkPosition)
        return true
    }

    override fun load(dataLoader: DataLoader) {
        this.claimTable.clear()

        for (claim: Claim in dataLoader.loadClaims()) {
            this.claimTable.put(claim.world, claim.chunkPosition, claim)
        }
    }

    override fun save(dataLoader: DataLoader) {
        dataLoader.saveClaims(this.claimTable.values())
    }
}