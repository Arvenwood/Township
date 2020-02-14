package pw.dotdash.township.plugin.util

import org.spongepowered.api.CatalogType
import org.spongepowered.api.GameRegistry
import org.spongepowered.api.registry.CatalogRegistryModule

inline fun <reified T> GameRegistry.registerBuilderSupplier(noinline supplier: () -> T): GameRegistry =
    this.registerBuilderSupplier(T::class.java, supplier)

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
inline fun <reified T : CatalogType> GameRegistry.registerModule(module: CatalogRegistryModule<T>): GameRegistry =
    this.registerModule(T::class.java, module)