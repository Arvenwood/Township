package pw.dotdash.township.plugin.permission

import org.spongepowered.api.registry.AdditionalCatalogRegistryModule
import org.spongepowered.api.registry.util.RegisterCatalog
import pw.dotdash.township.api.permission.ClaimPermission
import pw.dotdash.township.api.permission.ClaimPermissions
import pw.dotdash.township.plugin.permission.util.AbstractPrefixAlternativeRegistryModule

@RegisterCatalog(ClaimPermissions::class)
class ClaimPermissionRegistryModule :
    AbstractPrefixAlternativeRegistryModule<ClaimPermission>("township"),
    AdditionalCatalogRegistryModule<ClaimPermission> {

    override fun registerDefaults() {
        this.register(ClaimPermission.builder().id("township:break").name("Break").build())
        this.register(ClaimPermission.builder().id("township:container_use").name("Container Use").build())
        this.register(ClaimPermission.builder().id("township:entity_spawning").name("Entity Spawning").build())
        this.register(ClaimPermission.builder().id("township:explosions").name("Explosions").build())
        this.register(ClaimPermission.builder().id("township:item_use").name("Item Use").build())
        this.register(ClaimPermission.builder().id("township:modify").name("Modify").build())
        this.register(ClaimPermission.builder().id("township:place").name("Place").build())
        this.register(ClaimPermission.builder().id("township:pvp").name("PvP").build())
        this.register(ClaimPermission.builder().id("township:pve").name("PvE").build())
        this.register(ClaimPermission.builder().id("township:switch").name("Switch").build())
    }

    override fun registerAdditionalCatalog(extraCatalog: ClaimPermission) {
        this.register(extraCatalog)
    }
}