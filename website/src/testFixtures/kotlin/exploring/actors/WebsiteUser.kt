package exploring.actors

import exploring.Actor
import exploring.dto.ItemId
import exploring.dto.OrderId
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.events.Events
import org.http4k.webdriver.Http4kWebDriver
import org.openqa.selenium.By

class WebsiteUser(
    events: Events,
    rawHttp: HttpHandler,
    private val baseUri: Uri
) : Actor("Website User", rawHttp, events) {

    private val browser = Http4kWebDriver(http)

    fun login() = with(browser) {
        navigate().to(baseUri)
        findElement(By.id("email"))!!.sendKeys("joe@http4k.org")
        findElement(By.tagName("form"))!!.submit()
    }

    fun listItems() = with(browser) {
        navigate().to(baseUri)
        (findElements(By.tagName("form")) ?: emptyList())
            .map { ItemId.of(it.getAttribute("action").substringAfterLast('/')) }
    }

    fun order(id: ItemId): OrderId = with(browser) {
        findElement(By.id("ITEM$id"))?.submit()
        return OrderId.of(findElement(By.id("orderId"))!!.text.toInt())
    }
}
