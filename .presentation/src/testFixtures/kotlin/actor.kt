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

class Customer(baseUri: Uri, http: HttpHandler, clock: Clock, events: Events) {
    private val http = SetBaseUriFrom(baseUri)
        .then(AppOutgoingHttp(false, AppEvents("Website User", clock, events), http))

    fun listItems() = http(Request(GET, "/list"))

    fun order(id: ItemId) = http(Request(POST, "/order/$id"))
}

fun main() {
    Customer(TODO(), TODO(), TODO(), TODO()).listItems()
    Customer(TODO(), TODO(), TODO(), TODO()).order(ItemId.of("!23"))
}
