package exploring

import exploring.actors.WebsiteUser
import org.http4k.core.Uri
import org.http4k.events.then
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class ClusterTest : TracingTest() {
    private val theInternet = TheInternet()
    private val cluster = Cluster(theInternet, events.then(::println))

    private val user = WebsiteUser(events, cluster, Uri.of("http://api-gateway"))

    @Test
    fun `can load stock list and order item`() {
        with(user) {
            login()

            val catalogue = listItems()
            val itemId = catalogue.first()
            val orderId = order(itemId)
            expectThat(theInternet.departmentStore.orders[orderId]?.items).isEqualTo(listOf(itemId))
        }
    }
}
