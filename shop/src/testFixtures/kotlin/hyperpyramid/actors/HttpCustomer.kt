package hyperpyramid.actors

import hyperpyramid.app.AppEvents
import hyperpyramid.app.AppOutgoingHttp
import hyperpyramid.dto.Email
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
import org.http4k.core.with
import org.http4k.events.Events
import org.http4k.filter.ClientFilters.ResetRequestTracing
import org.http4k.filter.ClientFilters.SetHostFrom
import org.http4k.lens.Query
import org.http4k.lens.value
import java.time.Clock

open class HttpCustomer(baseUri: Uri, clock: Clock, events: Events, http: HttpHandler) : Customer {
    val http = ResetRequestTracing()
        .then(SetHostFrom(baseUri))
        .then(AppOutgoingHttp(AppEvents("Customer", clock, events), http))

    val email = Email.of("joe@http4k.org")

    override fun listItems(): List<ItemId> {
        val response = http(Request(GET, "/list"))
        return Body.auto<List<ItemId>>().toLens()(response)
    }

    override fun order(id: ItemId): OrderId {
        val response = http(Request(POST, "/order/$id").with(Query.value(Email).required("email") of email))
        return Body.auto<OrderId>().toLens()(response)
    }
}
