package pw.dotdash.township.plugin.permission

import org.spongepowered.api.registry.AdditionalCatalogRegistryModule
import org.spongepowered.api.registry.util.RegisterCatalog
import org.spongepowered.api.text.Text
import pw.dotdash.township.api.permission.ClaimPermission
import pw.dotdash.township.api.permission.ClaimPermissions
import pw.dotdash.township.plugin.permission.util.AbstractPrefixAlternativeRegistryModule

@RegisterCatalog(ClaimPermissions::class)
class ClaimPermissionRegistryModule :
    AbstractPrefixAlternativeRegistryModule<ClaimPermission>("township"),
    AdditionalCatalogRegistryModule<ClaimPermission> {

    override fun registerDefaults() {
        this.register(
            ClaimPermission.builder()
                .id("township:break")
                .name("Break")
                .description(Text.of("Allows residents to break blocks in a claim."))
                .build()
        )
        this.register(
            ClaimPermission.builder()
                .id("township:container_use")
                .name("Container Use")
                .description(Text.of("Allows residents to open chests, furnaces, etc. in a claim."))
                .build()
        )
        this.register(
            ClaimPermission.builder()
                .id("township:entity_spawning")
                .name("Entity Spawning")
                .description(Text.of("Allows entities to spawn in a claim."))
                .build()
        )
        this.register(
            ClaimPermission.builder()
                .id("township:explosions")
                .name("Explosions")
                .description(Text.of("Allows explosions to trigger in a claim."))
                .build()
        )
        this.register(
            ClaimPermission.builder()
                .id("township:item_use")
                .name("Item Use")
                .description(Text.of("Allows residents to use items in a claim."))
                .build()
        )
        this.register(
            ClaimPermission.builder()
                .id("township:modify")
                .name("Modify")
                .description(Text.of("Allows residents to modify blocks in a claim."))
                .build()
        )
        this.register(
            ClaimPermission.builder()
                .id("township:place")
                .name("Place")
                .description(Text.of("Allows residents to place blocks in a claim."))
                .build()
        )
        this.register(
            ClaimPermission.builder()
                .id("township:pvp")
                .name("PvP")
                .description(Text.of("Allows residents to attack other players in a claim."))
                .build()
        )
        this.register(
            ClaimPermission.builder()
                .id("township:pve")
                .name("PvE")
                .description(Text.of("Allows residents to attack non-player entities in a claim."))
                .build()
        )
        this.register(
            ClaimPermission.builder()
                .id("township:switch")
                .name("Switch")
                .description(Text.of("Allows residents to open doors, flip switches, etc. in a claim."))
                .build()
        )
    }

    override fun registerAdditionalCatalog(extraCatalog: ClaimPermission) {
        this.register(extraCatalog)
    }
}