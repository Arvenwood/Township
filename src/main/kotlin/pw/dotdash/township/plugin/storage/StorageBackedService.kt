package pw.dotdash.township.plugin.storage

interface StorageBackedService {

    fun load(dataLoader: DataLoader)

    fun save(dataLoader: DataLoader)
}