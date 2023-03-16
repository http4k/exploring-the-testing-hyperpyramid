package hyperpyramid.actor

import hyperpyramid.Actor
import hyperpyramid.dto.ItemId
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.events.Events

class WarehouseClient(http: HttpHandler, events: Events = {}) : Actor("client", http, events) {
    fun listItems() = http(Request(GET, "http://warehouse/v1/items"))

    fun dispatchAn(itemId: ItemId) =
        http(
            Request(POST, "http://warehouse/v1/dispatch")
                .body("""{"customer":"bob@http4k.org","id":"$itemId","amount":1}""")
        )
}

