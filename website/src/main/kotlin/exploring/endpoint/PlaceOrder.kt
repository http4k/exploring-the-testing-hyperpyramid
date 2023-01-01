package exploring.endpoint

import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.orThrow
import exploring.dto.ItemId
import exploring.port.WebsiteHub
import org.http4k.connect.amazon.sns.model.PhoneNumber
import org.http4k.core.Method.POST
import org.http4k.lens.Path
import org.http4k.lens.value
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.template.TemplateRenderer
import org.http4k.template.ViewModel
import org.http4k.template.renderToResponse

fun PlaceOrder(hub: WebsiteHub, templates: TemplateRenderer): RoutingHttpHandler = "/order/{id}" bind POST to {
    hub.order(PhoneNumber.of("01234567890"), Path.value(ItemId).of("id")(it))
        .map { templates.renderToResponse(OrderPlaced) }
        .orThrow()
}

object OrderPlaced : ViewModel
