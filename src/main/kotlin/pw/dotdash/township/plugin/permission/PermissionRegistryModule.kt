package pw.dotdash.township.plugin.permission

import pw.dotdash.township.api.permission.Permission
import pw.dotdash.township.plugin.permission.util.AbstractPrefixCheckRegistryModule

class PermissionRegistryModule(
    private val claimPermissions: ClaimPermissionRegistryModule,
    private val townPermissions: TownPermissionRegistryModule
) : AbstractPrefixCheckRegistryModule<Permission>("township") {

    override fun registerDefaults() {
        this.claimPermissions.all.forEach(this::register)
        this.townPermissions.all.forEach(this::register)
    }
}