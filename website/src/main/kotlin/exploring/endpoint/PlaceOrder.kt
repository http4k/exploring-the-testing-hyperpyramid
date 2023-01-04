package exploring.endpoint

import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.orThrow
import exploring.dto.Email
import exploring.dto.ItemId
import exploring.dto.OrderId
import exploring.port.WebsiteHub
import exploring.util.Json
import org.http4k.base64Decoded
import org.http4k.core.Method.POST
import org.http4k.lens.Cookies
import org.http4k.lens.Path
import org.http4k.lens.value
import org.http4k.routing.bind
import org.http4k.template.TemplateRenderer
import org.http4k.template.ViewModel
import org.http4k.template.renderToResponse

fun PlaceOrder(hub: WebsiteHub, templates: TemplateRenderer) = "/order/{id}" bind POST to {
    hub.order(emailClaimFromJwt(it), itemId(it))
        .map { templates.renderToResponse(OrderPlaced(it)) }
        .orThrow()
}

data class OrderPlaced(val orderId: OrderId) : ViewModel

private val itemId = Path.value(ItemId).of("id")

private val emailClaimFromJwt = Cookies.map { it.value.split('.')[1].base64Decoded() }
    .map<Map<String, String>>(Json::asA)
    .map { Email.of(it["email"]!!) }
    .required("AccessToken")
