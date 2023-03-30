package hyperpyramid.endpoint

import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.orThrow
import hyperpyramid.dto.InventoryItem
import hyperpyramid.port.Shop
import org.http4k.core.Method.GET
import org.http4k.routing.bind
import org.http4k.template.TemplateRenderer
import org.http4k.template.ViewModel
import org.http4k.template.renderToResponse

fun ListAllItems(shop: Shop, templates: TemplateRenderer) = "/" bind GET to {
    shop.items()
        .map(::Items)
        .map(templates::renderToResponse)
        .orThrow()
}

data class Items(val inventory: List<InventoryItem>) : ViewModel
