package exploring.adapter

import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.resultFrom
import exploring.WarehouseSettings.INVENTORY_DB_TABLE
import exploring.dto.InventoryItem
import exploring.port.Inventory
import org.http4k.cloudnative.env.Environment
import org.http4k.connect.amazon.dynamodb.DynamoDb
import org.http4k.connect.amazon.dynamodb.Http
import org.http4k.connect.amazon.dynamodb.mapper.DynamoDbTableMapperSchema.Primary
import org.http4k.connect.amazon.dynamodb.mapper.tableMapper
import org.http4k.connect.amazon.dynamodb.model.Attribute
import org.http4k.core.HttpHandler
import java.util.UUID

fun Inventory.Companion.DynamoDb(env: Environment, http: HttpHandler) =
    object : Inventory {
        private val index = Primary(Attribute.uuid().required("id"), Attribute.string().required("name"))
        private val table =
            DynamoDb.Http(env, http).tableMapper<InventoryItem, UUID, String>(INVENTORY_DB_TABLE(env), index)

        override fun items() = resultFrom { table.index(index).query() }.map { it.toList() }

        override fun store(item: InventoryItem) = resultFrom { table += item }

        override fun adjust(id: UUID, amount: Int) = resultFrom {
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

