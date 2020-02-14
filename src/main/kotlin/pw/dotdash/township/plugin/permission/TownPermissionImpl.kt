package pw.dotdash.township.plugin.permission

import pw.dotdash.township.api.permission.Permission
import pw.dotdash.township.api.permission.TownPermission

data class TownPermissionImpl(
    private val id: String,
    private val name: String
) : TownPermission {

    override fun getId(): String = this.id

    override fun getName(): String = this.name

    override fun toString(): String = this.id

    class Builder : TownPermission.Builder {

        private var id: String? = null
        private var name: String? = null

        override fun id(id: String): Builder {
            this.id = id
            return this
        }

        override fun name(name: String): Builder {
            this.name = name
            return this
        }

        override fun from(value: Permission): Builder {
            this.id = value.id
            this.name = value.name
            return this
        }

        override fun reset(): Builder {
            this.id = null
            this.name = null
            return this
        }

        override fun build(): TownPermission {
            val id: String = checkNotNull(this.id)
            return TownPermissionImpl(
                id = id,
                name = this.name ?: id
            )
        }
    }
}