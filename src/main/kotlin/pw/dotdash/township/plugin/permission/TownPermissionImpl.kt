package pw.dotdash.township.plugin.permission

import org.spongepowered.api.text.Text
import pw.dotdash.township.api.permission.Permission
import pw.dotdash.township.api.permission.TownPermission
import pw.dotdash.township.plugin.util.unwrap
import java.util.*

data class TownPermissionImpl(
    private val id: String,
    private val name: String,
    private val description: Optional<Text>
) : TownPermission {

    override fun getId(): String = this.id

    override fun getName(): String = this.name

    override fun getDescription(): Optional<Text> = this.description

    override fun toString(): String = this.id

    class Builder : TownPermission.Builder {

        private var id: String? = null
        private var name: String? = null
        private var description: Text? = null

        override fun id(id: String): Builder {
            this.id = id
            return this
        }

        override fun name(name: String): Builder {
            this.name = name
            return this
        }

        override fun description(description: Text): TownPermission.Builder {
            this.description = description
            return this
        }

        override fun from(value: TownPermission): TownPermission.Builder {
            this.id = value.id
            this.name = value.name
            this.description = value.description.unwrap()
            return this
        }

        override fun reset(): Builder {
            this.id = null
            this.name = null
            this.description = null
            return this
        }

        override fun build(): TownPermission {
            val id: String = checkNotNull(this.id)
            return TownPermissionImpl(
                id = id,
                name = this.name ?: id,
                description = Optional.ofNullable(this.description)
            )
        }
    }
}