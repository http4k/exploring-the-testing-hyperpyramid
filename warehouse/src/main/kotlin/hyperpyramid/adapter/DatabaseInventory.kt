package hyperpyramid.adapter

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import hyperpyramid.WarehouseSettings.DATABASE_DRIVER
import hyperpyramid.WarehouseSettings.DATABASE_URL
import hyperpyramid.port.Inventory
import org.http4k.cloudnative.env.Environment
import org.http4k.connect.storage.Jdbc
import org.http4k.connect.storage.Storage
import org.http4k.connect.storage.StorageTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction

fun Inventory.Companion.Database(env: Environment): Inventory {
    val ds = HikariDataSource(HikariConfig().apply {
        driverClassName = DATABASE_DRIVER(env)
        jdbcUrl = DATABASE_URL(env)
    })

    transaction(Database.connect(ds)) { create(StorageTable("InventoryItem")) }

    return Inventory.Storage(Storage.Jdbc(ds))
}

