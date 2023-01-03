package exploring.adapter

import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Success
import exploring.WarehouseSettings.STORE_URL
import exploring.dto.Order
import exploring.dto.OrderId
import exploring.port.DepartmentStore
import exploring.util.Json.auto
import org.http4k.cloudnative.env.Environment
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.then
import org.http4k.core.with
import org.http4k.filter.ClientFilters.SetHostFrom

fun DepartmentStore.Companion.Http(env: Environment, http: HttpHandler): DepartmentStore {
    val http = SetHostFrom(STORE_URL(env)).then(http)

    return DepartmentStore { phone, item ->
        val resp = http(
            Request(POST, "/v1/order")
                .with(Body.auto<Order>().toLens() of Order(phone, listOf(item)))
        )
        when {
            resp.status.successful -> Success(OrderId.parse(resp.bodyString()))
            else -> Failure(Exception("oh no!"))
        }
    }
}
