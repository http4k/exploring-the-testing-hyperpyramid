package hyperpyramid.adapter

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import hyperpyramid.WarehouseSettings.DATABASE_DRIVER
import hyperpyramid.WarehouseSettings.DATABASE_URL
import hyperpyramid.port.Inventory
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

