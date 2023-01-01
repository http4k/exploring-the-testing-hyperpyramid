package exploring.actors

import exploring.Actor
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.events.Events
import org.http4k.filter.ClientFilters.Cookies
import org.http4k.filter.ClientFilters.FollowRedirects
import java.util.UUID

class WebsiteUser(
    events: Events,
    http: HttpHandler,
    private val baseUri: Uri
) : Actor("Website User", http, events) {

    private val browser = FollowRedirects()
        .then(Cookies())
        .then(http)

    fun listItems() = browser(Request(GET, baseUri.path("/")))

    fun order(id: UUID) = browser(Request(POST, baseUri.path("/order/$id")))
}
