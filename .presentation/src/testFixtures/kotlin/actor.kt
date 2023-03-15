import hyperpyramid.app.AppEvents
import hyperpyramid.app.AppOutgoingHttp
import hyperpyramid.dto.ItemId
import hyperpyramid.dto.OrderId
import org.http4k.connect.amazon.cognito.CognitoMoshi.auto
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.events.Events
import org.http4k.filter.ClientFilters.SetBaseUriFrom
import java.time.Clock
    
class Customer(clock: Clock, events: Events, http: HttpHandler, baseUri: Uri) {
    private val http = SetBaseUriFrom(baseUri)
        .then(AppOutgoingHttp(false, AppEvents("Website User", clock, events), http))

    fun listItems(): List<ItemId> {
        val response = http(Request(GET, "/list"))
        return Body.auto<List<ItemId>>().toLens()(response)
    }

    fun order(id: ItemId): OrderId {
        val response = http(Request(POST, "/order/$id"))
        return Body.auto<OrderId>().toLens()(response)
    }
}
