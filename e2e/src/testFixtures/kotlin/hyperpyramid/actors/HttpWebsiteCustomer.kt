package hyperpyramid.actors

import hyperpyramid.dto.ItemId
import hyperpyramid.dto.OrderId
import org.http4k.connect.amazon.ses.EmailMessage
import org.http4k.connect.amazon.ses.model.EmailAddress
import org.http4k.connect.storage.Storage
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Uri
import org.http4k.core.extend
import org.http4k.events.Events
import java.time.Clock

class HttpWebsiteCustomer(
    private val baseUri: Uri,
    clock: Clock,
    events: Events,
    http: HttpHandler,
    private val sentEmails: Storage<List<EmailMessage>>
) : HttpCustomer(baseUri, clock, events, http), WebsiteCustomer {

    override fun canSeeImage(id: ItemId) = http(Request(GET, baseUri.extend(Uri.of("/img/$id")))).status == OK

    override fun hasEmailFor(orderId: OrderId, email: String): Boolean {
        return sentEmails.keySet()
            .flatMap { sentEmails[it] ?: emptyList() }
            .filter { it.to.contains(EmailAddress.of(email)) }
            .any { it.message.html?.value == "Please collect your order using the code: $orderId" }
    }
}
