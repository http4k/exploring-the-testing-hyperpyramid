package exploring

import dev.forkhandles.result4k.valueOrNull
import exploring.ApiGatewaySettings.API_GATEWAY_URL
import exploring.ApiGatewaySettings.IMAGES_URL
import exploring.ApiGatewaySettings.OAUTH_CLIENT_ID
import exploring.ApiGatewaySettings.OAUTH_CLIENT_SECRET
import exploring.ApiGatewaySettings.WEBSITE_URL
import exploring.ImageSettings.IMAGE_BUCKET
import exploring.WarehouseSettings.STORE_API_PASSWORD
import exploring.WarehouseSettings.STORE_API_USER
import exploring.WarehouseSettings.STORE_URL
import exploring.WebsiteSettings.NOTIFICATION_EMAIL_SENDER
import exploring.WebsiteSettings.WAREHOUSE_URL
import exploring.adapter.InMemory
import exploring.dto.Email
import exploring.port.Inventory
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.Environment.Companion.defaults
import org.http4k.connect.amazon.AWS_ACCESS_KEY_ID
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.AWS_SECRET_ACCESS_KEY
import org.http4k.connect.amazon.cognito.Cognito
import org.http4k.connect.amazon.cognito.Http
import org.http4k.connect.amazon.cognito.createUserPool
import org.http4k.connect.amazon.cognito.createUserPoolClient
import org.http4k.connect.amazon.cognito.model.ClientName
import org.http4k.connect.amazon.cognito.model.PoolName
import org.http4k.connect.amazon.core.model.AccessKeyId
import org.http4k.connect.amazon.core.model.Region.Companion.EU_WEST_1
import org.http4k.connect.amazon.core.model.SecretAccessKey
import org.http4k.connect.amazon.s3.Http
import org.http4k.connect.amazon.s3.S3
import org.http4k.connect.amazon.s3.S3Bucket
import org.http4k.connect.amazon.s3.createBucket
import org.http4k.connect.amazon.s3.model.BucketKey
import org.http4k.connect.amazon.s3.model.BucketName
import org.http4k.connect.amazon.s3.putObject
import org.http4k.core.Credentials
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Uri
import org.http4k.core.with
import org.http4k.events.Events
import org.http4k.routing.asRouter
import org.http4k.routing.bind
import org.http4k.routing.reverseProxyRouting
import org.http4k.routing.routes
import java.time.Clock

fun Cluster(
    customEnv: Environment,
    theInternet: HttpHandler,
    events: Events = ::println,
    clock: Clock = Clock.systemUTC()
): HttpHandler {
    val baseEnv = customEnv overrides defaults(
        AWS_REGION of EU_WEST_1,
        AWS_ACCESS_KEY_ID of AccessKeyId.of("access-key-id"),
        AWS_SECRET_ACCESS_KEY of SecretAccessKey.of("secret-access-key"),
        IMAGE_BUCKET of BucketName.of("image-cache"),
        IMAGES_URL of Uri.of("http://images"),
        NOTIFICATION_EMAIL_SENDER of Email.of("orders@http4k.org"),
        STORE_URL of Uri.of("http://dept-store"),
        STORE_API_USER of "user",
        STORE_API_PASSWORD of "password",
        WAREHOUSE_URL of Uri.of("http://warehouse"),
        WEBSITE_URL of Uri.of("http://website")
    )

    val env = populateCloudResources(baseEnv, theInternet)

    val networkAccess = NetworkAccess()

    val apiGateway = ApiGateway(env, events, clock, networkAccess)
    networkAccess.http = routes(
        reverseProxyRouting(
            API_GATEWAY_URL(env).host to apiGateway,
            IMAGES_URL(env).host to Images(env, events, clock, networkAccess),
            WAREHOUSE_URL(env).host to Warehouse(env, events, clock, networkAccess, Inventory.InMemory(events, clock)),
            WEBSITE_URL(env).host to Website(env, events, clock, networkAccess)
        ),
        { _: Request -> true }.asRouter() bind theInternet,
    )

    return networkAccess
}

private fun populateCloudResources(baseEnv: Environment, http: HttpHandler): Environment {
    // image bucket
    S3.Http(baseEnv, http).createBucket(IMAGE_BUCKET(baseEnv), AWS_REGION(baseEnv))
    S3Bucket.Http(IMAGE_BUCKET(baseEnv), AWS_REGION(baseEnv), baseEnv, http).apply {
        putObject(BucketKey.of("1"), "1".byteInputStream(), emptyList())
        putObject(BucketKey.of("2"), "2".byteInputStream(), emptyList())
        putObject(BucketKey.of("3"), "3".byteInputStream(), emptyList())
        putObject(BucketKey.of("4"), "4".byteInputStream(), emptyList())
    }

    // oauth client
    val oauthCredentials = with(Cognito.Http(baseEnv, http)) {
        val poolId = createUserPool(PoolName.of("pool")).valueOrNull()!!.UserPool.Id!!
        val userPoolClient = createUserPoolClient(poolId, ClientName.of("Hyperpyramid"), GenerateSecret = true)
            .valueOrNull()!!.UserPoolClient

        Credentials(userPoolClient.ClientId.value, userPoolClient.ClientSecret!!.value)
    }

    return baseEnv.with(
        OAUTH_CLIENT_ID of oauthCredentials.user,
        OAUTH_CLIENT_SECRET of oauthCredentials.password
    )
}
