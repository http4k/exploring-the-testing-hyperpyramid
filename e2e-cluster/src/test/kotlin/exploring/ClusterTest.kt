package exploring

import exploring.ApiGatewaySettings.API_GATEWAY_URL
import exploring.ApiGatewaySettings.OAUTH_CLIENT_ID
import exploring.ApiGatewaySettings.OAUTH_CLIENT_SECRET
import exploring.ApiGatewaySettings.OAUTH_URL
import exploring.ApiGatewaySettings.WEBSITE_URL
import exploring.WarehouseSettings.DATABASE_DRIVER
import exploring.WarehouseSettings.DATABASE_URL
import exploring.WarehouseSettings.STORE_API_PASSWORD
import exploring.WarehouseSettings.STORE_API_USER
import exploring.WarehouseSettings.STORE_URL
import exploring.WebsiteSettings.NOTIFICATION_EMAIL_SENDER
import exploring.WebsiteSettings.WAREHOUSE_URL
import exploring.actors.Customer
import exploring.dto.Email
import org.http4k.cloudnative.env.Environment.Companion.defaults
import org.http4k.connect.amazon.AWS_ACCESS_KEY_ID
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.AWS_SECRET_ACCESS_KEY
import org.http4k.connect.amazon.cognito.model.ClientId
import org.http4k.connect.amazon.cognito.registerOAuthClient
import org.http4k.connect.amazon.core.model.AccessKeyId
import org.http4k.connect.amazon.core.model.Region
import org.http4k.connect.amazon.core.model.SecretAccessKey
import org.http4k.core.Uri
import org.http4k.events.then
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isTrue

class ClusterTest : TracingTest() {
    private val theInternet = TheInternet()

    val oauthCredentials = theInternet.cognito.registerOAuthClient(ClientId.of("apiGateway"))

    val commonEnv = defaults(
        ApiGatewaySettings.DEBUG of false,
        AWS_REGION of Region.EU_WEST_1,
        AWS_ACCESS_KEY_ID of AccessKeyId.of("access-key-id"),
        AWS_SECRET_ACCESS_KEY of SecretAccessKey.of("secret-access-key"),
    )
    val apiGatewayEnv = defaults(
        API_GATEWAY_URL of Uri.of("http://api-gateway"),
        WEBSITE_URL of Uri.of("http://website"),
        OAUTH_URL of Uri.of("http://cognito"),
        OAUTH_CLIENT_ID of oauthCredentials.user,
        OAUTH_CLIENT_SECRET of oauthCredentials.password,
    )
    val websiteEnv = defaults(
        NOTIFICATION_EMAIL_SENDER of Email.of("orders@http4k.org"),
        WAREHOUSE_URL of Uri.of("http://warehouse"),
    )
    val warehouseEnv = defaults(
        DATABASE_URL of "jdbc:h2:mem:warehouse;DB_CLOSE_DELAY=-1",
        DATABASE_DRIVER of "org.h2.Driver",
        STORE_URL of Uri.of("http://dept-store"),
        STORE_API_USER of "user",
        STORE_API_PASSWORD of "password"
    )

    val env = commonEnv overrides apiGatewayEnv overrides websiteEnv overrides warehouseEnv

    private val cluster = Cluster(env, theInternet, events.then(::println))

    private val user = Customer(events, cluster, Uri.of("http://api-gateway"), theInternet.emailInbox)

    @Test
    fun `can load stock list and order item`() {
        with(user) {
            login()

            val catalogue = listItems()
            val itemId = catalogue.first()
            val orderId = order(itemId)
            expectThat(theInternet.departmentStore.orders[orderId]?.items).isEqualTo(listOf(itemId))

            expectThat(user.hasEmailFor(orderId)).isTrue()
        }
    }
}
