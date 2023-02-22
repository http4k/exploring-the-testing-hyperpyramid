package hyperpyramid.scenario

import hyperpyramid.actors.Customer
import org.http4k.core.HttpHandler
import org.http4k.core.Uri

interface WebsiteContract {
    val http: HttpHandler

    fun client() = Customer(http, Uri.of("http://website"), { emptyList() })
}
