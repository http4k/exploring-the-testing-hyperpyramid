package hyperpyramid.actors

import hyperpyramid.Actor
import hyperpyramid.dto.ItemId
import hyperpyramid.dto.OrderId
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.events.Events
import org.http4k.webdriver.Http4kWebDriver
import org.openqa.selenium.By

open class HttpCustomer(
    browser: HttpHandler,
    protected val baseUri: Uri,
    events: Events = {}
) : Actor("Customer", browser, events), Customer {

    protected val browser = Http4kWebDriver(http)

    override fun listItems() = with(browser) {
        navigate().to(baseUri)
        (findElements(By.tagName("form")) ?: emptyList())
            .map { ItemId.of(it.getAttribute("action").substringAfterLast('/')) }
    }

    override fun order(id: ItemId): OrderId = with(browser) {
        findElement(By.id("ITEM$id"))?.submit()
        return OrderId.of(findElement(By.id("orderId"))!!.text.toInt())
    }
}
