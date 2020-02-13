package arvenwood.towns.plugin.storage

import arvenwood.towns.api.claim.Claim
import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.town.Town
import arvenwood.towns.api.warp.Warp

interface DataLoader {

    fun loadClaims(): Collection<Claim>

    fun saveClaims(claims: Collection<Claim>)

    fun loadResidents(): Collection<Resident>

    fun saveResidents(residents: Collection<Resident>)

    fun loadTowns(): Collection<Town>

    fun saveTowns(towns: Collection<Town>)

    fun loadWarps(): Collection<Warp>

    fun saveWarps(warps: Collection<Warp>)
}