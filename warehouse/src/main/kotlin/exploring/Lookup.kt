package exploring

import org.http4k.connect.amazon.dynamodb.DynamoDb
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind

fun LookupItem(http: DynamoDb) = "/v1/item/{id}" bind Method.GET to { req: Request -> Response(Status.OK) }
