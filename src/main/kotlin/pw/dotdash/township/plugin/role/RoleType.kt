package pw.dotdash.township.plugin.role

import pw.dotdash.township.api.permission.ClaimPermission
import pw.dotdash.township.api.permission.Permission
import pw.dotdash.township.api.permission.TownPermission

enum class RoleType {
    TOWN {
        override fun isValid(permission: Permission): Boolean {
            return permission is TownPermission || permission is ClaimPermission
        }
    };

    abstract fun isValid(permission: Permission): Boolean
}