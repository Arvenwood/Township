package arvenwood.towns.plugin.storage

interface StorageBackedService {

    fun load(dataLoader: DataLoader)

    fun save(dataLoader: DataLoader)
}