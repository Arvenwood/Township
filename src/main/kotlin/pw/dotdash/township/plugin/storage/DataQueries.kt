package pw.dotdash.township.plugin.storage

import org.spongepowered.api.data.DataQuery

object DataQueries {

    val CHUNK_POSITION_X: DataQuery = DataQuery.of("ChunkPositionX")

    val CHUNK_POSITION_Y: DataQuery = DataQuery.of("ChunkPositionY")

    val CHUNK_POSITION_Z: DataQuery = DataQuery.of("ChunkPositionZ")

    val CLAIMS: DataQuery = DataQuery.of("Claims")

    val LOCATION: DataQuery = DataQuery.of("Location")

    val NAME: DataQuery = DataQuery.of("Name")

    val OPEN: DataQuery = DataQuery.of("Open")

    val OWNER_UNIQUE_ID: DataQuery = DataQuery.of("OwnerUniqueId")

    val RESIDENTS: DataQuery = DataQuery.of("Residents")

    val RESIDENT_UNIQUE_IDS: DataQuery = DataQuery.of("ResidentUniqueIds")

    val TOWNS: DataQuery = DataQuery.of("Towns")

    val TOWN_UNIQUE_ID: DataQuery = DataQuery.of("TownUniqueId")

    val TOWN_NAME: DataQuery = DataQuery.of("Name")

    val UNIQUE_ID: DataQuery = DataQuery.of("UniqueId")

    val WARPS: DataQuery = DataQuery.of("Warps")

    val WORLD_UNIQUE_ID: DataQuery = DataQuery.of("WorldUniqueId")
}