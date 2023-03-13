import hyperpyramid.actor.Actor
import hyperpyramid.dto.ItemId
import hyperpyramid.dto.OrderId
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.events.Events
import org.http4k.filter.ClientFilters.SetBaseUriFrom

class Customer(http: HttpHandler, private val baseUri: Uri, events: Events = {}) :
    Actor("Website User", http, events) {

    fun listItems() = SetBaseUriFrom(baseUri).then(http)(Request(GET, "/list"))

    fun order(id: ItemId) =
        SetBaseUriFrom(baseUri).then(http)(Request(POST, "/order/$id"))
            .let { OrderId.parse(it.bodyString()) }
}

fun main() {
    Customer(TODO(), TODO(), TODO()).listItems()
    Customer(TODO(), TODO(), TODO()).order(ItemId.of("!23"))
}
