package pw.dotdash.township.plugin.permission.util

import org.spongepowered.api.CatalogType
import org.spongepowered.api.registry.AlternateCatalogRegistryModule

abstract class AbstractPrefixAlternativeRegistryModule<T : CatalogType>(defaultNamespace: String) :
    AbstractPrefixCheckRegistryModule<T>(defaultNamespace), AlternateCatalogRegistryModule<T> {

    override fun provideCatalogMap(): MutableMap<String, T> {
        val result = HashMap<String, T>()

        for ((id: String, value: T) in this.catalogMap) {
            result[id.replace("${this.defaultNamespace}:", "")] = value
            result[value.name.toLowerCase()] = value
        }

        return result
    }
}