package exploring

import dev.forkhandles.result4k.valueOrNull
import exploring.ApiGatewaySettings.API_GATEWAY_URL
import exploring.ApiGatewaySettings.DEBUG
import exploring.ApiGatewaySettings.OAUTH_CLIENT_ID
import exploring.ApiGatewaySettings.OAUTH_CLIENT_SECRET
import exploring.ApiGatewaySettings.OAUTH_URL
import exploring.ImageSettings.IMAGE_BUCKET
import exploring.actors.Customer
import org.http4k.cloudnative.env.Environment.Companion.defaults
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.cognito.createUserPool
import org.http4k.connect.amazon.cognito.createUserPoolClient
import org.http4k.connect.amazon.cognito.model.ClientName
import org.http4k.connect.amazon.cognito.model.PoolName
import org.http4k.connect.amazon.core.model.Region.Companion.EU_WEST_1
import org.http4k.connect.amazon.s3.createBucket
import org.http4k.connect.amazon.s3.model.BucketKey
import org.http4k.connect.amazon.s3.model.BucketName
import org.http4k.connect.amazon.s3.putObject
import org.http4k.core.Credentials
import org.http4k.core.Uri
import org.http4k.core.with
import org.http4k.events.then
import org.http4k.filter.debug
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isTrue

class ClusterTest : TracingTest() {
    private val theInternet = TheInternet()

    private val env = run {
        val baseEnv = defaults(
            DEBUG of false,
            AWS_REGION of EU_WEST_1,
            API_GATEWAY_URL of Uri.of("http://api-gateway"),
            IMAGE_BUCKET of BucketName.of("image-cache"),
            OAUTH_URL of Uri.of("http://cognito-idp.$EU_WEST_1.amazonaws.com"),
        )

        // images
        with(theInternet.s3) {
            s3Client().createBucket(IMAGE_BUCKET(baseEnv), EU_WEST_1)
            s3BucketClient(IMAGE_BUCKET(baseEnv), EU_WEST_1).apply {
                putObject(BucketKey.of("1"), "1".byteInputStream(), emptyList())
                putObject(BucketKey.of("2"), "2".byteInputStream(), emptyList())
                putObject(BucketKey.of("3"), "3".byteInputStream(), emptyList())
                putObject(BucketKey.of("4"), "4".byteInputStream(), emptyList())
            }
        }

        // oauth client
        val creds = with(theInternet.cognito.client()) {
            val poolId = createUserPool(PoolName.of("pool")).valueOrNull()!!.UserPool.Id!!
            val userPoolClient = createUserPoolClient(
                UserPoolId = poolId,
                ClientName = ClientName.of("Hyperpyramid"),
                GenerateSecret = true
            )
                .valueOrNull()!!.UserPoolClient

            Credentials(userPoolClient.ClientId.value, userPoolClient.ClientSecret!!.value)
        }

        baseEnv.with(
            OAUTH_CLIENT_ID of creds.user,
            OAUTH_CLIENT_SECRET of creds.password
        )
    }

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
