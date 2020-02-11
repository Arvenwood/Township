package arvenwood.towns.api.claim

import arvenwood.towns.api.town.Town
import org.spongepowered.api.Sponge
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World

interface ClaimService {

    companion object {
        @JvmStatic
        fun get(): ClaimService =
            Sponge.getServiceManager().provideUnchecked(ClaimService::class.java)
    }

    fun getClaimAt(location: Location<World>): Claim?

    fun getClaims(world: World): Collection<Claim>

    fun getClaimsFor(town: Town): Collection<Claim>

    fun register(claim: Claim): Boolean

    fun unregister(claim: Claim): Boolean
}