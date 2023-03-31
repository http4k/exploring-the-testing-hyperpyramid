package hyperpyramid.actors

import hyperpyramid.dto.Email
import hyperpyramid.dto.ItemId
import hyperpyramid.dto.OrderId
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Uri
import org.http4k.core.extend
import org.http4k.events.Events
import org.openqa.selenium.By

class HttpWebsiteCustomer(http: HttpHandler, baseUri: Uri, private val emailInbox: Emails, events: Events = {}) :
    HttpCustomer(http, baseUri, events), WebsiteCustomer {
    override fun login() = with(browser) {
        navigate().to(baseUri)
        findElement(By.id("email"))!!.sendKeys(email.value)
        findElement(By.tagName("form"))!!.submit()
    }

    override fun canSeeImage(id: ItemId) = http(Request(GET, baseUri.extend(Uri.of("/img/$id")))).status == OK

    override fun hasEmailFor(orderId: OrderId) = emailInbox(email)
        .any { it.second == "Please collect your order using the code: $orderId" }

    private val email = Email.of("joe@http4k.org")
}
