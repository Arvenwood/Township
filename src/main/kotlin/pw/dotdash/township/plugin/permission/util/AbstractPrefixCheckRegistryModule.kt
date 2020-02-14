package pw.dotdash.township.plugin.permission.util

import org.spongepowered.api.CatalogType
import org.spongepowered.api.registry.CatalogRegistryModule
import java.util.*
import java.util.concurrent.ConcurrentHashMap

abstract class AbstractPrefixCheckRegistryModule<T : CatalogType>(val defaultNamespace: String) : CatalogRegistryModule<T> {

    @JvmField
    protected val catalogMap = ConcurrentHashMap<String, T>()

    override fun getById(id: String): Optional<T> {
        var key: String = id.toLowerCase(Locale.ENGLISH)

        if (':' !in key) {
            key = "${this.defaultNamespace}:$key"
        }

        return Optional.ofNullable(this.catalogMap[key])
    }

    override fun getAll(): Collection<T> =
        Collections.unmodifiableCollection(this.catalogMap.values)

    protected fun register(catalog: T) {
        this.catalogMap[catalog.id] = catalog
    }
}