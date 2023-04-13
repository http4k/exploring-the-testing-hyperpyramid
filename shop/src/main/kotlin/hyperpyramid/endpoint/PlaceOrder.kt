package hyperpyramid.endpoint

import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.orThrow
import hyperpyramid.dto.Email
import hyperpyramid.dto.ItemId
import hyperpyramid.port.Shop
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.lens.Path
import org.http4k.lens.Query
import org.http4k.lens.value
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind

fun PlaceOrder(shop: Shop): RoutingHttpHandler = "/order/{id}" bind POST to {
    shop.order(email(it), itemId(it))
        .map { Response(OK).body(it.value.toString()) }
        .orThrow()
}

private val itemId = Path.value(ItemId).of("id")
private val email = Query.value(Email).required("email")
