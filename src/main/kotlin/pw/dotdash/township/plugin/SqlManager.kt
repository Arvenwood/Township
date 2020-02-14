package pw.dotdash.township.plugin

import org.spongepowered.api.Sponge
import org.spongepowered.api.service.sql.SqlService
import java.nio.file.Path
import javax.sql.DataSource

object SqlManager {

    lateinit var dataSource: DataSource
        private set

    fun init(plugin: Any, dialect: Dialect) {
        this.dataSource = Sponge.getServiceManager().provideUnchecked(SqlService::class.java)
            .getDataSource(plugin, dialect.jdbcUrl)
    }

    sealed class Dialect(val jdbcUrl: String) {

        data class H2(val path: Path) : Dialect("jdbc:h2:$path")
    }
}