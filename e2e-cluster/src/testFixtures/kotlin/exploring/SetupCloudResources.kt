package exploring

import exploring.WarehouseSettings.DISPATCH_QUEUE
import exploring.WarehouseSettings.INVENTORY_DB_TABLE
import exploring.WebsiteSettings.NOTIFICATION_TOPIC_ARN
import exploring.adapter.DynamoDb
import exploring.adapter.InventorySchema.hash
import exploring.adapter.InventorySchema.sort
import exploring.dto.InventoryItem
import exploring.dto.ItemId
import exploring.port.Inventory
import org.http4k.cloudnative.env.Environment
import org.http4k.connect.amazon.dynamodb.DynamoDb
import org.http4k.connect.amazon.dynamodb.Http
import org.http4k.connect.amazon.dynamodb.createTable
import org.http4k.connect.amazon.dynamodb.model.KeySchema
import org.http4k.connect.amazon.dynamodb.model.asAttributeDefinition
import org.http4k.connect.amazon.dynamodb.model.compound
import org.http4k.connect.amazon.sns.Http
import org.http4k.connect.amazon.sns.SNS
import org.http4k.connect.amazon.sns.createTopic
import org.http4k.connect.amazon.sns.model.TopicName
import org.http4k.connect.amazon.sqs.Http
import org.http4k.connect.amazon.sqs.SQS
import org.http4k.connect.amazon.sqs.createQueue
import org.http4k.connect.amazon.sqs.model.QueueName
import org.http4k.core.HttpHandler
import org.http4k.core.Uri

fun setupCloudResources(env: Environment, theInternet: HttpHandler) {
    DynamoDb.Http(env, theInternet).createTable(
        INVENTORY_DB_TABLE(env),
        KeySchema.compound(hash.name, sort.name),
        listOf(hash.asAttributeDefinition(), sort.asAttributeDefinition())
    )
    Inventory.DynamoDb(env, theInternet).apply {
        store(InventoryItem(ItemId.of("1"), "Banana", 5))
        store(InventoryItem(ItemId.of("2"), "Bottom", 1))
        store(InventoryItem(ItemId.of("3"), "Minion Toys", 100))
        store(InventoryItem(ItemId.of("4"), "Guitar", 12))
    }

    SQS.Http(env, theInternet).createQueue(
        QueueName.parse(DISPATCH_QUEUE(env)),
        emptyList(),
        emptyMap()
    )

    SNS.Http(env, theInternet)
        .createTopic(NOTIFICATION_TOPIC_ARN(env).resourceId(TopicName::of), emptyList(), emptyMap())
}

private fun QueueName.Companion.parse(uri: Uri) = QueueName.of(uri.path.substringAfterLast('/'))
