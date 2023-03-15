import hyperpyramid.dto.Order
import hyperpyramid.dto.OrderId
import org.http4k.core.Body
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import org.http4k.lens.Path
import org.http4k.lens.value
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

fun FakeDeptStore(): RoutingHttpHandler {
    val orders = mutableMapOf<OrderId, Order>()

    return routes(
        "/v1/order" bind POST to { req: Request ->
            val orderId = OrderId.of(orders.size + 1)
            orders[orderId] = orderLens(req)

            Response(OK).body(orderId.toString())
        },
        "/v1/order/{id}" bind GET to { req: Request ->
            orders[id(req)]
                ?.let { Response(OK).with(orderLens of it) }
                ?: Response(NOT_FOUND)
        }
    )
}

fun main() {
    FakeDeptStore().asServer(SunHttp()).start()
}

private val orderLens = Body.auto<Order>().toLens()
private val id = Path.value(OrderId).of("id")

object TestClock : Clock() {
    override fun instant(): Instant {
        TODO("Not yet implemented")
    }

    override fun withZone(zone: ZoneId?): Clock {
        TODO("Not yet implemented")
    }

    override fun getZone(): ZoneId {
        TODO("Not yet implemented")
    }
}
