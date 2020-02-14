package pw.dotdash.township.plugin.permission

import org.spongepowered.api.registry.AdditionalCatalogRegistryModule
import org.spongepowered.api.registry.util.RegisterCatalog
import pw.dotdash.township.api.permission.TownPermission
import pw.dotdash.township.api.permission.TownPermissions
import pw.dotdash.township.plugin.permission.util.AbstractPrefixAlternativeRegistryModule

@RegisterCatalog(TownPermissions::class)
class TownPermissionRegistryModule :
    AbstractPrefixAlternativeRegistryModule<TownPermission>("township"),
    AdditionalCatalogRegistryModule<TownPermission> {


    override fun registerDefaults() {
        this.register(TownPermission.builder().id("township:bank_deposit").name("Bank Deposit").build())
        this.register(TownPermission.builder().id("township:bank_withdraw").name("Bank Withdraw").build())
        this.register(TownPermission.builder().id("township:kick_residents").name("Kick Residents").build())
        this.register(TownPermission.builder().id("township:manage_claims").name("Manage Claims").build())
        this.register(TownPermission.builder().id("township:manage_permissions").name("Manage Permissions").build())
        this.register(TownPermission.builder().id("township:town_chat").name("Town Chat").build())
    }

    override fun registerAdditionalCatalog(extraCatalog: TownPermission) {
        this.register(extraCatalog)
    }
}