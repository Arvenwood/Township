package pw.dotdash.township.plugin.permission

import org.spongepowered.api.registry.AdditionalCatalogRegistryModule
import org.spongepowered.api.registry.util.RegisterCatalog
import org.spongepowered.api.text.Text
import pw.dotdash.township.api.permission.TownPermission
import pw.dotdash.township.api.permission.TownPermissions
import pw.dotdash.township.plugin.permission.util.AbstractPrefixAlternativeRegistryModule

@RegisterCatalog(TownPermissions::class)
class TownPermissionRegistryModule :
    AbstractPrefixAlternativeRegistryModule<TownPermission>("township"),
    AdditionalCatalogRegistryModule<TownPermission> {


    override fun registerDefaults() {
        this.register(
            TownPermission.builder()
                .id("township:bank_deposit")
                .name("Bank Deposit")
                .description(Text.of("Allows residents to deposit money into the town bank."))
                .build()
        )
        this.register(
            TownPermission.builder()
                .id("township:bank_withdraw")
                .name("Bank Withdraw")
                .description(Text.of("Allows residents to withdraw money from the town bank."))
                .build()
        )
        this.register(
            TownPermission.builder()
                .id("township:town_deputy")
                .name("Town Deputy")
                .description(Text.of("Allows residents ALL town permissions, and ignores claim overrides. Use with caution."))
                .build()
        )
        this.register(
            TownPermission.builder()
                .id("township:kick_residents")
                .name("Kick Residents")
                .description(Text.of("Allows residents to kick other residents from the town."))
                .build()
        )
        this.register(
            TownPermission.builder()
                .id("township:manage_claims")
                .name("Manage Claims")
                .description(Text.of("Allows residents to create and delete claims for the town."))
                .build()
        )
        this.register(
            TownPermission.builder()
                .id("township:manage_permissions")
                .name("Manage Permissions")
                .description(Text.of("Allows residents to change role permissions for the town."))
                .build()
        )
        this.register(
            TownPermission.builder()
                .id("township:town_chat")
                .name("Town Chat")
                .description(Text.of("Allows residents to talk in thw town chat channel."))
                .build()
        )
    }

    override fun registerAdditionalCatalog(extraCatalog: TownPermission) {
        this.register(extraCatalog)
    }
}