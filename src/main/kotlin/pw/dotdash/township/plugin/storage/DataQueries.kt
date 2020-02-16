package pw.dotdash.township.plugin.storage

import org.spongepowered.api.data.DataQuery

object DataQueries {

    val CHUNK_POSITION: DataQuery = DataQuery.of("ChunkPosition")

    val CLAIMS: DataQuery = DataQuery.of("Claims")

    val FRIENDS: DataQuery = DataQuery.of("Friends")

    val LOCATION: DataQuery = DataQuery.of("Location")

    val NAME: DataQuery = DataQuery.of("Name")

    val OPEN: DataQuery = DataQuery.of("Open")

    val OWNER_ID: DataQuery = DataQuery.of("OwnerId")

    val PERMISSIONS: DataQuery = DataQuery.of("Permissions")

    val PRIORITY: DataQuery = DataQuery.of("Priority")

    val RESIDENTS: DataQuery = DataQuery.of("Residents")

    val RESIDENT_UNIQUE_IDS: DataQuery = DataQuery.of("ResidentUniqueIds")

    val ROLES: DataQuery = DataQuery.of("Roles")

    val ROLE_PERMISSION_OVERRIDES: DataQuery = DataQuery.of("RolePermissionOverrides")

    val TOWNS: DataQuery = DataQuery.of("Towns")

    val TOWN_ID: DataQuery = DataQuery.of("TownId")

    val TOWN_ROLES: DataQuery = DataQuery.of("TownRoles")

    val UNIQUE_ID: DataQuery = DataQuery.of("UniqueId")

    val VISITOR_ROLE: DataQuery = DataQuery.of("VisitorRole")

    val WARPS: DataQuery = DataQuery.of("Warps")

    val WORLD_UNIQUE_ID: DataQuery = DataQuery.of("WorldUniqueId")
}