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

fun DatabaseInventory(env: Environment): Inventory {
    val ds = HikariDataSource(HikariConfig().apply {
        driverClassName = env[DATABASE_DRIVER]
        jdbcUrl = env[DATABASE_URL]
    })

    transaction(Database.connect(ds)) { create(StorageTable("InventoryItem")) }

    return StorageInventory(Storage.Jdbc(ds))
}

