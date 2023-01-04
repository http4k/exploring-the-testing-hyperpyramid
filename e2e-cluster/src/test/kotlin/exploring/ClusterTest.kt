package exploring

import exploring.ApiGatewaySettings.API_GATEWAY_URL
import exploring.ApiGatewaySettings.DEBUG
import exploring.ApiGatewaySettings.OAUTH_URL
import exploring.actors.Customer
import org.http4k.cloudnative.env.Environment.Companion.defaults
import org.http4k.connect.amazon.core.model.Region.Companion.EU_WEST_1
import org.http4k.core.Uri
import org.http4k.events.then
import org.http4k.filter.debug
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isTrue

class ClusterTest : TracingTest() {
    private val theInternet = TheInternet()

    private val env = defaults(
        DEBUG of false,
        API_GATEWAY_URL of Uri.of("http://api-gateway"),
        OAUTH_URL of Uri.of("http://cognito-idp.$EU_WEST_1.amazonaws.com"),
    )

    private val cluster = Cluster(env, theInternet.debug(), events.then(::println))

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
