package exploring

import exploring.dto.Order
import exploring.dto.OrderId
import exploring.util.Json.auto
import org.http4k.chaos.ChaoticHttpHandler
import org.http4k.core.Body
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.core.with
import org.http4k.filter.ServerFilters.BasicAuth
import org.http4k.lens.Path
import org.http4k.lens.value
import org.http4k.routing.bind
import org.http4k.routing.routes

class FakeDepartmentStore : ChaoticHttpHandler() {
    val orders = mutableMapOf<OrderId, Order>()

    private val orderLens = Body.auto<Order>().toLens()
    private val id = Path.value(OrderId).of("id")

    override val app = BasicAuth("realm") { true }
        .then(
            routes(
                "/v1/order" bind POST to { req: Request ->
                    val order = orderLens(req)
                    val orderId = OrderId.of(orders.size + 1)
                    orders[orderId] = order

                    Response(OK).body(orderId.toString())
                },
                "/v1/order/{id}" bind GET to { req: Request ->
                    val orderId = id(req)
                    orders[orderId]
                        ?.let { Response(OK).with(orderLens of it) }
                        ?: Response(NOT_FOUND)
                },
                "/" bind { req: Request -> Response(OK).body("Welcome to the Hyperpyramid Store!") }
            )
        )
}
