package exploring

import org.http4k.connect.amazon.dynamodb.FakeDynamoDb
import org.http4k.connect.amazon.sqs.FakeSQS
import org.http4k.core.Filter
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes

class FakeAws : RoutingHttpHandler {
    private val http = routes(
        "dynamo" bind GET to FakeDynamoDb(),
        "sqs" bind GET to FakeSQS()
    )

    override fun invoke(p1: Request) = http(p1)
    override fun match(request: Request) = http.match(request)
    override fun withBasePath(new: String) = http.withBasePath(new)
    override fun withFilter(new: Filter) = http.withFilter(new)
}
