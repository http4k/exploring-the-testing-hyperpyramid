package hyperpyramid.scenario

import hyperpyramid.actors.Customer
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.events.Events

interface WebsiteContract {
    val http: HttpHandler
    val events: Events

    fun client() = Customer(http, Uri.of("http://website"), { emptyList() }, events)
}
