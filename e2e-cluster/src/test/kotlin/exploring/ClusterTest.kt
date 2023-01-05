package exploring

import exploring.ImageSettings.IMAGE_BUCKET
import exploring.actors.Customer
import exploring.setup.setupCloudEnvironmentUsing
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.core.model.Region.Companion.EU_WEST_1
import org.http4k.connect.amazon.s3.model.BucketName
import org.http4k.events.then
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isTrue

class ClusterTest : TracingTest() {
    private val services = ServiceDiscovery()
    private val theInternet = TheInternet(services)

    private val env = theInternet.setupCloudEnvironmentUsing(
        AWS_REGION of EU_WEST_1,
        IMAGE_BUCKET of BucketName.of("image-cache"),
    )

    private val cluster = Cluster(env, services, theInternet, events.then(::println))

    private val user = Customer(events, cluster, services("api-gateway"), theInternet.emailInbox)

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
