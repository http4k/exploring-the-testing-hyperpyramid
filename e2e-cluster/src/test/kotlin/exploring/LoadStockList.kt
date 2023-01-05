package exploring

import exploring.actors.Customer
import org.http4k.core.HttpHandler
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isTrue

abstract class LoadStockList : TracingTest() {

    abstract val services: ServiceDiscovery

    protected val theInternet by lazy { TheInternet(services) }

    protected abstract val http: HttpHandler

    private val user by lazy { Customer(events, http, services("api-gateway"), theInternet.emailInbox) }

    @Test
    fun `can load stock list and order item`() {
        with(user) {
            val catalogue = loginAndListItems()

            val itemId = catalogue.first()
            expectThat(canSeeImage(itemId)).isTrue()

            val orderId = order(itemId)
            expectThat(theInternet.departmentStore.orders[orderId]?.items).isEqualTo(listOf(itemId))

            expectThat(user.hasEmailFor(orderId)).isTrue()
        }
    }
}
