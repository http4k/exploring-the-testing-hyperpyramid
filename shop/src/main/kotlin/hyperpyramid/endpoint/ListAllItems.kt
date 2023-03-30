package hyperpyramid.endpoint

import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.orThrow
import hyperpyramid.dto.InventoryItem
import hyperpyramid.port.WebsiteHub
import org.http4k.core.Method.GET
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.template.TemplateRenderer
import org.http4k.template.ViewModel
import org.http4k.template.renderToResponse

fun ListAllItems(hub: WebsiteHub, templates: TemplateRenderer): RoutingHttpHandler = "/" bind GET to {
    hub.items()
        .map { templates.renderToResponse(Items(it)) }
        .orThrow()
}

data class Items(val inventory: List<InventoryItem>) : ViewModel
