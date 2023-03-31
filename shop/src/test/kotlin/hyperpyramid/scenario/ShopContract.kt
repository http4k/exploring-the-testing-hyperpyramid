package hyperpyramid.scenario

import hyperpyramid.actors.HttpCustomer
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.events.Events

interface ShopContract {
    val http: HttpHandler
    val events: Events

    fun customer() = HttpCustomer(http, Uri.of("http://shop"), events)
}
