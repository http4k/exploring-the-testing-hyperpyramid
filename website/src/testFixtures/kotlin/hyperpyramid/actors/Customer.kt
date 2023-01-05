package hyperpyramid.actors

import hyperpyramid.actor.Actor
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
import org.http4k.webdriver.Http4kWebDriver
import org.openqa.selenium.By

class Customer(
    http: HttpHandler,
    private val baseUri: Uri,
    private val emailInbox: Emails,
    events: Events = {}
) : Actor("Website User", http, events) {

    private val email = Email.of("joe@http4k.org")

    private fun browser() = Http4kWebDriver(http)

    fun loginAndListItems() = with(browser()) {
        navigate().to(baseUri)
        findElement(By.id("email"))!!.sendKeys(email.value)
        findElement(By.tagName("form"))!!.submit()
        (findElements(By.tagName("form")) ?: emptyList())
            .map { ItemId.of(it.getAttribute("action").substringAfterLast('/')) }
    }

    fun canSeeImage(id: ItemId) = http(Request(GET, baseUri.extend(Uri.of("/img/$id")))).status == OK

    fun order(id: ItemId): OrderId = with(browser()) {
        findElement(By.id("ITEM$id"))?.submit()
        return OrderId.of(findElement(By.id("orderId"))!!.text.toInt())
    }

    fun hasEmailFor(orderId: OrderId) = emailInbox(email)
        .any { it.second == "Please collect your order using the code: $orderId" }
}
