package exploring.adapter

import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result4k
import dev.forkhandles.result4k.Success
import exploring.dto.ItemId
import exploring.dto.Order
import exploring.dto.OrderId
import exploring.dto.Phone
import exploring.port.DepartmentStore
import exploring.util.Json.auto
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.core.with
import org.http4k.filter.ClientFilters.SetHostFrom

fun DepartmentStore.Companion.Http(base: Uri, http: HttpHandler): DepartmentStore {
    val http = SetHostFrom(base).then(http)

    return object : DepartmentStore {
        override fun collection(phone: Phone, item: ItemId): Result4k<OrderId, Exception> {
            val resp = http(
                Request(POST, "/v1/order")
                    .with(Body.auto<Order>().toLens() of Order(phone, listOf(item)))
            )
            return when {
                resp.status.successful -> Success(OrderId.parse(resp.bodyString()))
                else -> Failure(Exception("oh no!"))
            }
        }

        override fun lookup(orderId: OrderId): Result4k<Order?, Exception> {
            val resp = http(Request(GET, "/v1/order/$orderId"))

            return when {
                resp.status.successful -> Success(Body.auto<Order>().toLens()(resp))
                resp.status == NOT_FOUND -> Success(null)
                else -> Failure(Exception("oh no!"))
            }
        }
    }
}
