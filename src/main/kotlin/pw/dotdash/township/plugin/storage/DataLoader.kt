package pw.dotdash.township.plugin.storage

import pw.dotdash.township.api.claim.Claim
import pw.dotdash.township.api.resident.Resident
import pw.dotdash.township.api.role.Role
import pw.dotdash.township.api.role.TownRole
import pw.dotdash.township.api.town.Town
import pw.dotdash.township.api.warp.Warp

interface DataLoader {

    fun loadClaims(): Collection<Claim>

    fun saveClaims(claims: Collection<Claim>)

    fun loadResidents(): Collection<Resident>

    fun saveResidents(residents: Collection<Resident>)

    fun loadTownRoles(): Collection<TownRole>

    fun saveTownRoles(roles: Collection<TownRole>)

    fun loadTowns(): Collection<Town>

    fun saveTowns(towns: Collection<Town>)

    fun loadWarps(): Collection<Warp>

    fun saveWarps(warps: Collection<Warp>)
}