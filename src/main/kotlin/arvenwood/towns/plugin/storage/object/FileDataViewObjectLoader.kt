package arvenwood.towns.plugin.storage.`object`

import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.data.DataView
import org.spongepowered.api.data.persistence.DataFormat
import java.nio.file.Files
import java.nio.file.Path

data class FileDataViewObjectLoader(val path: Path, val format: DataFormat) :
    ObjectLoader<DataView, DataContainer> {

    override fun load(): DataContainer =
        this.format.readFrom(Files.newInputStream(this.path))

    override fun save(input: DataView) {
        this.format.writeTo(Files.newOutputStream(this.path), input)
    }
}