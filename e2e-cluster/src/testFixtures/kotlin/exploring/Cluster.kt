package exploring

import dev.forkhandles.result4k.valueOrNull
import exploring.ApiGatewaySettings.OAUTH_CLIENT_ID
import exploring.ApiGatewaySettings.OAUTH_CLIENT_SECRET
import exploring.ImageSettings.IMAGE_BUCKET
import exploring.adapter.InMemory
import exploring.port.Inventory
import org.http4k.cloudnative.env.Environment
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.cognito.Cognito
import org.http4k.connect.amazon.cognito.Http
import org.http4k.connect.amazon.cognito.createUserPool
import org.http4k.connect.amazon.cognito.createUserPoolClient
import org.http4k.connect.amazon.cognito.model.ClientName
import org.http4k.connect.amazon.cognito.model.PoolName
import org.http4k.connect.amazon.s3.Http
import org.http4k.connect.amazon.s3.S3
import org.http4k.connect.amazon.s3.S3Bucket
import org.http4k.connect.amazon.s3.createBucket
import org.http4k.connect.amazon.s3.model.BucketKey
import org.http4k.connect.amazon.s3.putObject
import org.http4k.core.Credentials
import org.http4k.core.HttpHandler
import org.http4k.core.with
import org.http4k.events.Events
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.reverseProxyRouting
import org.http4k.routing.routes
import java.time.Clock

fun Cluster(
    env: Environment,
    theInternet: RoutingHttpHandler,
    events: Events = ::println,
    clock: Clock = Clock.systemUTC()
): HttpHandler {
    val networkAccess = NetworkAccess()

    val env = populateCloudResources(env, theInternet)

    networkAccess.http = routes(
        theInternet,
        reverseProxyRouting(
            "api-gateway" to ApiGateway(env, events, clock, networkAccess),
            "images" to Images(env, events, clock, networkAccess),
            "warehouse" to Warehouse(env, events, clock, networkAccess, Inventory.InMemory(events, clock)),
            "website" to Website(env, events, clock, networkAccess)
        )
    )

    return networkAccess
}

private fun populateCloudResources(env: Environment, http: HttpHandler): Environment {

    // image bucket
    S3.Http(env, http).createBucket(IMAGE_BUCKET(env), AWS_REGION(env))
    S3Bucket.Http(IMAGE_BUCKET(env), AWS_REGION(env), env, http).apply {
        putObject(BucketKey.of("1"), "1".byteInputStream(), emptyList())
        putObject(BucketKey.of("2"), "2".byteInputStream(), emptyList())
        putObject(BucketKey.of("3"), "3".byteInputStream(), emptyList())
        putObject(BucketKey.of("4"), "4".byteInputStream(), emptyList())
    }

    // oauth client
    val oauthCredentials = with(Cognito.Http(env, http)) {
        val poolId = createUserPool(PoolName.of("pool")).valueOrNull()!!.UserPool.Id!!
        val userPoolClient = createUserPoolClient(poolId, ClientName.of("Exploring"), GenerateSecret = true)
            .valueOrNull()!!.UserPoolClient

        Credentials(userPoolClient.ClientId.value, userPoolClient.ClientSecret!!.value)
    }

    return env.with(
        OAUTH_CLIENT_ID of oauthCredentials.user,
        OAUTH_CLIENT_SECRET of oauthCredentials.password
    )
}
