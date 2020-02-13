package arvenwood.towns.plugin.util

import org.spongepowered.api.service.ServiceManager

inline fun <reified T> ServiceManager.setProvider(plugin: Any, provider: T): ServiceManager {
    this.setProvider(plugin, T::class.java, provider)
    return this
}