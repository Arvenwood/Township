package pw.dotdash.township.plugin.storage.`object`

import org.spongepowered.api.Sponge
import org.spongepowered.api.data.DataSerializable
import org.spongepowered.api.data.DataView
import pw.dotdash.township.plugin.util.unwrap

data class DataViewObjectSerializer<T : DataSerializable>(val type: Class<T>) :
    ObjectSerializer<T, DataView, DataView> {

    companion object {
        inline operator fun <reified T : DataSerializable> invoke(): DataViewObjectSerializer<T> =
            DataViewObjectSerializer(T::class.java)
    }

    override fun deserialize(input: DataView): T? =
        Sponge.getDataManager().deserialize(this.type, input).unwrap()

    override fun serialize(value: T): DataView =
        value.toContainer()
}