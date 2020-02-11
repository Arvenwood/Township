package arvenwood.towns.plugin.claim

import arvenwood.towns.api.claim.Claim
import arvenwood.towns.api.claim.ClaimService
import arvenwood.towns.api.town.Town
import com.flowpowered.math.vector.Vector3i
import com.google.common.collect.Table
import com.google.common.collect.Tables
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World

class SimpleClaimService : ClaimService {

    private val claimMap: Table<World, Vector3i, Claim> =
        Tables.newCustomTable(HashMap()) { HashMap<Vector3i, Claim>() }

    override fun getClaimAt(location: Location<World>): Claim? =
        this.claimMap[location.extent, location.chunkPosition]

    override fun getClaims(world: World): Collection<Claim> =
        this.claimMap.row(world).values.toSet()

    override fun getClaimsFor(town: Town): Collection<Claim> =
        this.claimMap.values().filter { it.town == town }

    override fun register(claim: Claim): Boolean {
        if (claim.chunkPosition in this.claimMap.row(claim.world)) {
            return false
        }

        this.claimMap.put(claim.world, claim.chunkPosition, claim)
        return true
    }

    override fun unregister(claim: Claim): Boolean {
        if (claim.chunkPosition !in this.claimMap.row(claim.world)) {
            return false
        }

        this.claimMap.remove(claim.world, claim.chunkPosition)
        return true
    }
}