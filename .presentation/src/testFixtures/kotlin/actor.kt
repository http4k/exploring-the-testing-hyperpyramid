import hyperpyramid.actor.Actor
import hyperpyramid.dto.ItemId
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.events.Events
import org.http4k.webdriver.Http4kWebDriver
import org.openqa.selenium.By

class Customer(http: HttpHandler, private val baseUri: Uri, events: Events = {}) :
    Actor("Website User", http, events) {

    private val browser = Http4kWebDriver(this.http)

    fun listItems() = with(browser) {
        navigate().to(baseUri)
        (findElements(By.tagName("form")) ?: emptyList())
            .map { ItemId.of(it.getAttribute("action").substringAfterLast('/')) }
    }
}

fun main() {
    Customer(TODO(), TODO(), TODO()).listItems()
}
