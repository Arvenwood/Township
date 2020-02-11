package arvenwood.towns.plugin.claim

import arvenwood.towns.api.claim.Claim
import arvenwood.towns.api.claim.ClaimService
import arvenwood.towns.api.town.Town
import arvenwood.towns.plugin.event.claim.CreateClaimEventImpl
import arvenwood.towns.plugin.event.claim.DeleteClaimEventImpl
import com.flowpowered.math.vector.Vector3i
import com.google.common.collect.Table
import com.google.common.collect.Tables
import org.spongepowered.api.Sponge
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import java.util.*
import kotlin.collections.HashMap

class SimpleClaimService : ClaimService {

    private val claimMap: Table<World, Vector3i, Claim> =
        Tables.newCustomTable(HashMap()) { HashMap<Vector3i, Claim>() }

    override fun getClaimAt(location: Location<World>): Optional<Claim> =
        Optional.ofNullable(this.claimMap[location.extent, location.chunkPosition])

    override fun getClaimsFor(world: World): Collection<Claim> =
        this.claimMap.row(world).values.toSet()

    override fun getClaimsFor(town: Town): Collection<Claim> =
        this.claimMap.values().filter { it.town == town }

    override fun register(claim: Claim): Boolean {
        if (claim.chunkPosition in this.claimMap.row(claim.world)) {
            return false
        }

        val event = CreateClaimEventImpl(claim, Sponge.getCauseStackManager().currentCause)
        Sponge.getEventManager().post(event)
        if (event.isCancelled) {
            return false
        }

        this.claimMap.put(claim.world, claim.chunkPosition, claim)
        return true
    }

    override fun unregister(claim: Claim): Boolean {
        if (claim.chunkPosition !in this.claimMap.row(claim.world)) {
            return false
        }

        val event = DeleteClaimEventImpl(claim, Sponge.getCauseStackManager().currentCause)
        Sponge.getEventManager().post(event)
        if (event.isCancelled) {
            return false
        }

        this.claimMap.remove(claim.world, claim.chunkPosition)
        return true
    }
}