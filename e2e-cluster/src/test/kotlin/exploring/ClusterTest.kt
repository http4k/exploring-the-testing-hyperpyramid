package exploring

import exploring.ApiGatewaySettings.API_GATEWAY_URL
import exploring.ApiGatewaySettings.OAUTH_URL
import exploring.ImageSettings.IMAGE_BUCKET
import exploring.actors.Customer
import exploring.setup.PopulateImageServer
import exploring.setup.SetupOAuthConnection
import exploring.setup.invoke
import org.http4k.cloudnative.env.Environment.Companion.defaults
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.core.model.Region.Companion.EU_WEST_1
import org.http4k.connect.amazon.s3.model.BucketName
import org.http4k.core.Uri
import org.http4k.events.then
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isTrue

class ClusterTest : TracingTest() {
    private val theInternet = TheInternet()

    private val baseEnv = defaults(
        AWS_REGION of EU_WEST_1,
        API_GATEWAY_URL of Uri.of("http://api-gateway"),
        IMAGE_BUCKET of BucketName.of("image-cache"),
        OAUTH_URL of Uri.of("http://cognito-idp.$EU_WEST_1.amazonaws.com"),
    )

    private val env = listOf(::SetupOAuthConnection, ::PopulateImageServer).map { it(theInternet) }(baseEnv)

    private val cluster = Cluster(env, theInternet, events.then(::println))

    private val user = Customer(events, cluster, API_GATEWAY_URL(env), theInternet.emailInbox)

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
