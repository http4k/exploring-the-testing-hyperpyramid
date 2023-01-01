package exploring

import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.connect.amazon.dynamodb.model.TableName
import org.http4k.lens.of
import org.http4k.lens.uri
import org.http4k.lens.value

object WarehouseSettings : Settings() {
    val INVENTORY_DB_TABLE by EnvironmentKey.value(TableName).of().required()
    val DISPATCH_QUEUE by EnvironmentKey.uri().of().required()
}
