package exploring.adapter

import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.resultFrom
import exploring.WarehouseSettings.INVENTORY_DB_TABLE
import exploring.adapter.InventorySchema.hash
import exploring.adapter.InventorySchema.sort
import exploring.dto.InventoryItem
import exploring.dto.ItemId
import exploring.port.Inventory
import org.http4k.cloudnative.env.Environment
import org.http4k.connect.amazon.dynamodb.DynamoDb
import org.http4k.connect.amazon.dynamodb.Http
import org.http4k.connect.amazon.dynamodb.mapper.tableMapper
import org.http4k.connect.amazon.dynamodb.model.Attribute
import org.http4k.connect.amazon.dynamodb.model.value
import org.http4k.core.HttpHandler

fun Inventory.Companion.DynamoDb(env: Environment, http: HttpHandler) =
    object : Inventory {
        private val table = DynamoDb.Http(env, http)
            .tableMapper<InventoryItem, ItemId, String>(INVENTORY_DB_TABLE(env), hash, sort)

        override fun items() = resultFrom { table.primaryIndex().query() }.map { it.toList() }

        override fun store(item: InventoryItem) = resultFrom { table += item }

        override fun adjust(id: ItemId, amount: Int) = resultFrom {
            table[id]
                ?.let { it.copy(stock = it.stock - amount) }
                ?.let {
                    when (it.stock) {
                        0 -> table -= it
                        else -> table += it
                    }
                }
        }
    }

object InventorySchema {
    val hash = Attribute.value(ItemId).required("id")
    val sort = Attribute.string().required("name")
}

