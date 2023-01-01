package exploring.actors

import exploring.Actor
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.events.Events
import org.http4k.filter.ClientFilters.SetBaseUriFrom
import org.http4k.webdriver.Http4kWebDriver
import org.openqa.selenium.By

class WebsiteUser(
    events: Events,
    rawHttp: HttpHandler,
    baseUri: Uri
) : Actor("Website User", rawHttp, events) {

    private val browser = Http4kWebDriver(
        SetBaseUriFrom(baseUri)
            .then(http)
    )

    fun listItems() = with(browser) {
        navigate().to(Uri.of("/"))
        (findElements(By.tagName("form")) ?: emptyList())
            .map { it.getAttribute("action").substringAfterLast('/').toLong() }
    }

    fun order(id: Long) = with(browser) {
        findElement(By.id("ITEM$id"))?.submit()
    }
}
