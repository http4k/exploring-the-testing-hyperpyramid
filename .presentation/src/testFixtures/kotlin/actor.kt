import hyperpyramid.app.AppEvents
import hyperpyramid.app.AppOutgoingHttp
import hyperpyramid.dto.ItemId
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.events.Events
import org.http4k.filter.ClientFilters.SetBaseUriFrom
import java.time.Clock

class Customer(events: Events, http: HttpHandler, clock: Clock, baseUri: Uri) {
    private val http = SetBaseUriFrom(baseUri)
        .then(AppOutgoingHttp(false, AppEvents("Website User", clock, events), http))

    fun listItems() = http(Request(GET, "/list"))

    fun order(id: ItemId) = http(Request(POST, "/order/$id"))
}
