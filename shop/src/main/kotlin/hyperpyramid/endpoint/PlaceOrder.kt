package hyperpyramid.endpoint

import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.orThrow
import hyperpyramid.dto.Email
import hyperpyramid.dto.ItemId
import hyperpyramid.dto.OrderId
import hyperpyramid.port.Shop
import hyperpyramid.util.Json
import org.http4k.base64Decoded
import org.http4k.core.Method.POST
import org.http4k.lens.Cookies
import org.http4k.lens.Path
import org.http4k.lens.value
import org.http4k.routing.bind
import org.http4k.template.TemplateRenderer
import org.http4k.template.ViewModel
import org.http4k.template.renderToResponse

fun PlaceOrder(shop: Shop, templates: TemplateRenderer) = "/order/{id}" bind POST to {
    shop.order(emailClaimFromJwt(it), itemId(it))
        .map(::OrderPlaced)
        .map(templates::renderToResponse)
        .orThrow()
}

data class OrderPlaced(val orderId: OrderId) : ViewModel

private val itemId = Path.value(ItemId).of("id")

private val emailClaimFromJwt = Cookies.map { it.value.split('.')[1].base64Decoded() }
    .map<Map<String, String>>(Json::asA)
    .map { Email.of(it["email"]!!) }
    .required("AccessToken")
