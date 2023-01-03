package exploring

import exploring.dto.Order
import exploring.dto.OrderId
import exploring.util.Json.auto
import org.http4k.chaos.ChaoticHttpHandler
import org.http4k.core.Body
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes

class FakeDepartmentStore : ChaoticHttpHandler() {
    val orders = mutableMapOf<OrderId, Order>()

    override val app = routes(
        "/v1/order" bind POST to { req: Request ->
            val order = Body.auto<Order>().toLens()(req)
            val orderId = OrderId.of(orders.size + 1)
            orders[orderId] = order

            Response(OK).body(orderId.toString())
        }
    )
}
