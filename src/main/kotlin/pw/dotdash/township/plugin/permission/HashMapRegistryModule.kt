package pw.dotdash.township.plugin.permission

import org.spongepowered.api.CatalogType
import org.spongepowered.api.registry.AdditionalCatalogRegistryModule
import java.util.*
import kotlin.collections.HashMap

abstract class HashMapRegistryModule<T : CatalogType> : AdditionalCatalogRegistryModule<T> {

    protected val map = HashMap<String, T>()

    override fun getById(id: String): Optional<T> =
        Optional.ofNullable(this.map[id.toLowerCase()])

    override fun getAll(): Collection<T> =
        map.values

    override fun registerAdditionalCatalog(extraCatalog: T) {
        this.map[extraCatalog.id.toLowerCase()] = extraCatalog
    }
}