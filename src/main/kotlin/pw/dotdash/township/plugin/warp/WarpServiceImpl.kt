package pw.dotdash.township.plugin.warp

import pw.dotdash.township.api.town.Town
import pw.dotdash.township.api.warp.Warp
import pw.dotdash.township.api.warp.WarpService
import pw.dotdash.township.plugin.storage.DataLoader
import pw.dotdash.township.plugin.storage.StorageBackedService
import com.google.common.collect.Table
import com.google.common.collect.Tables
import java.util.*

class WarpServiceImpl : WarpService, StorageBackedService {

    private val townToNameTable: Table<UUID, String, Warp> =
        Tables.newCustomTable(HashMap()) { HashMap<String, Warp>() }

    override fun getWarps(): Collection<Warp> =
        this.townToNameTable.values().toSet()

    override fun getWarp(uniqueId: UUID): Optional<Warp> =
        Optional.ofNullable(this.townToNameTable.values().find { it.uniqueId == uniqueId })

    override fun getWarpsByTown(town: Town): Collection<Warp> =
        this.townToNameTable.row(town.uniqueId).values.toSet()

    override fun getWarp(town: Town, name: String): Optional<Warp> =
        Optional.ofNullable(this.townToNameTable[town.uniqueId, name])

    override fun contains(warp: Warp): Boolean =
        this.townToNameTable.contains(warp.uniqueId, warp.name)

    override fun register(warp: Warp): Boolean {
        if (warp in this) {
            return false
        }

        this.townToNameTable.put(warp.uniqueId, warp.name, warp)
        return true
    }

    override fun unregister(warp: Warp): Boolean {
        if (warp !in this) {
            return false
        }

        this.townToNameTable.remove(warp.uniqueId, warp.name)
        return true
    }

    override fun load(dataLoader: DataLoader) {
        this.townToNameTable.clear()

        for (warp: Warp in dataLoader.loadWarps()) {
            this.townToNameTable.put(warp.townId, warp.name, warp)
        }
    }

    override fun save(dataLoader: DataLoader) {
        dataLoader.saveWarps(this.townToNameTable.values())
    }
}