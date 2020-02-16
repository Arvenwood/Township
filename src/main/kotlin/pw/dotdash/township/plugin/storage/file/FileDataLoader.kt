package pw.dotdash.township.plugin.storage.file

import pw.dotdash.township.api.claim.Claim
import pw.dotdash.township.api.resident.Resident
import pw.dotdash.township.api.town.Town
import pw.dotdash.township.api.warp.Warp
import pw.dotdash.township.plugin.storage.DataLoader
import pw.dotdash.township.plugin.storage.DataQueries
import pw.dotdash.township.plugin.storage.`object`.FileDataViewObjectLoader
import pw.dotdash.township.plugin.storage.`object`.ObjectLoader
import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.data.DataQuery
import org.spongepowered.api.data.DataSerializable
import org.spongepowered.api.data.DataView
import org.spongepowered.api.data.persistence.DataFormat
import pw.dotdash.township.api.town.TownRole
import java.nio.file.Files
import java.nio.file.Path

data class FileDataLoader(val root: Path, val format: DataFormat) : DataLoader {

    private val pathClaims: Path = this.root.resolve("claims.dat")
    private val pathResidents: Path = this.root.resolve("residents.dat")
    private val pathRoles: Path = this.root.resolve("roles.dat")
    private val pathTowns: Path = this.root.resolve("towns.dat")
    private val pathWarps: Path = this.root.resolve("warps.dat")

    private val loaderClaims = FileDataViewObjectLoader(this.pathClaims, this.format)
    private val loaderResidents = FileDataViewObjectLoader(this.pathResidents, this.format)
    private val loaderRoles = FileDataViewObjectLoader(this.pathRoles, this.format)
    private val loaderTowns = FileDataViewObjectLoader(this.pathTowns, this.format)
    private val loaderWarps = FileDataViewObjectLoader(this.pathWarps, this.format)

    init {
        if (Files.notExists(this.root)) Files.createDirectories(this.root)

        if (Files.notExists(this.pathClaims)) Files.createFile(this.pathClaims)
        if (Files.notExists(this.pathResidents)) Files.createFile(this.pathResidents)
        if (Files.notExists(this.pathRoles)) Files.createFile(this.pathRoles)
        if (Files.notExists(this.pathTowns)) Files.createFile(this.pathTowns)
        if (Files.notExists(this.pathWarps)) Files.createFile(this.pathWarps)
    }

    private fun <T : DataSerializable> loadValues(loader: ObjectLoader<DataView, DataView>, type: Class<T>, query: DataQuery): List<T> {
        val view: DataView = try {
            loader.load()
        } catch (e: Exception) {
            DataContainer.createNew()
        }

        return view.getSerializableList(query, type).orElse(emptyList())
    }

    private fun <T : DataSerializable> saveValues(loader: ObjectLoader<DataView, DataView>, values: Collection<T>, query: DataQuery) {
        if (values.isEmpty()) {
            // Nothing to save.
            return
        }

        loader.save(DataContainer.createNew().set(query, values.map(DataSerializable::toContainer)))
    }

    override fun loadClaims(): Collection<Claim> =
        this.loadValues(this.loaderClaims, Claim::class.java, DataQueries.CLAIMS)

    override fun saveClaims(claims: Collection<Claim>): Unit =
        this.saveValues(this.loaderClaims, claims, DataQueries.CLAIMS)

    override fun loadResidents(): Collection<Resident> =
        this.loadValues(this.loaderResidents, Resident::class.java, DataQueries.RESIDENTS)

    override fun saveResidents(residents: Collection<Resident>): Unit =
        this.saveValues(this.loaderResidents, residents, DataQueries.RESIDENTS)

    override fun loadTownRoles(): Collection<TownRole> =
        this.loadValues(this.loaderRoles, TownRole::class.java, DataQueries.ROLES)

    override fun saveTownRoles(roles: Collection<TownRole>): Unit =
        this.saveValues(this.loaderRoles, roles, DataQueries.ROLES)

    override fun loadTowns(): Collection<Town> =
        this.loadValues(this.loaderTowns, Town::class.java, DataQueries.TOWNS)

    override fun saveTowns(towns: Collection<Town>): Unit =
        this.saveValues(this.loaderTowns, towns, DataQueries.TOWNS)

    override fun loadWarps(): Collection<Warp> =
        this.loadValues(this.loaderWarps, Warp::class.java, DataQueries.WARPS)

    override fun saveWarps(warps: Collection<Warp>): Unit =
        this.saveValues(this.loaderWarps, warps, DataQueries.WARPS)
}