package pw.dotdash.township.plugin.util

import org.spongepowered.api.data.DataManager
import org.spongepowered.api.data.DataSerializable
import org.spongepowered.api.data.persistence.DataBuilder

inline fun <reified T : DataSerializable> DataManager.registerBuilder(builder: DataBuilder<T>): DataManager {
    this.registerBuilder(T::class.java, builder)
    return this
}