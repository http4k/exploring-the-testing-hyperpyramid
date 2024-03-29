package hyperpyramid.actors

import hyperpyramid.app.AppEvents
import hyperpyramid.app.AppOutgoingHttp
import hyperpyramid.dto.ItemId
import hyperpyramid.dto.OrderId
import hyperpyramid.util.Json.auto
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.events.Events
import org.http4k.filter.ClientFilters.ResetRequestTracing
import org.http4k.filter.ClientFilters.SetHostFrom
import java.time.Clock

open class HttpCustomer(baseUri: Uri, clock: Clock, events: Events, http: HttpHandler) : Customer {
    val http = ResetRequestTracing()
        .then(SetHostFrom(baseUri))
        .then(AppOutgoingHttp(AppEvents("Customer", clock, events), http))

    override fun listItems(): List<ItemId> {
        val response = http(Request(GET, "/list"))
        return Body.auto<List<ItemId>>().toLens()(response)
    }

    override fun order(id: ItemId, email: String): OrderId {
        val response = http(Request(POST, "/order/$id").query("email", email))
        return Body.auto<OrderId>().toLens()(response)
    }
}
