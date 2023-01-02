package exploring.adapter

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import exploring.WarehouseSettings.DATABASE_DRIVER
import exploring.WarehouseSettings.DATABASE_URL
import exploring.port.Inventory
import org.http4k.cloudnative.env.Environment
import org.http4k.connect.storage.Jdbc
import org.http4k.connect.storage.Storage

fun Inventory.Companion.Database(env: Environment) = Inventory.Storage(
    Storage.Jdbc(
        HikariDataSource(HikariConfig().apply {
            driverClassName = DATABASE_DRIVER(env)
            jdbcUrl = DATABASE_URL(env)
        })
    )
)

