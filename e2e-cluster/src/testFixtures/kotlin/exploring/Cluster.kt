package exploring

import exploring.WarehouseSettings.DATABASE_DRIVER
import exploring.WarehouseSettings.DATABASE_URL
import exploring.WarehouseSettings.DISPATCH_QUEUE
import exploring.WebsiteSettings.NOTIFICATION_TOPIC_ARN
import exploring.adapter.Fake
import exploring.port.Inventory
import org.http4k.cloudnative.env.Environment
import org.http4k.connect.amazon.AWS_ACCESS_KEY_ID
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.AWS_SECRET_ACCESS_KEY
import org.http4k.connect.amazon.core.model.ARN
import org.http4k.connect.amazon.core.model.AccessKeyId
import org.http4k.connect.amazon.core.model.Region.Companion.EU_WEST_1
import org.http4k.connect.amazon.core.model.SecretAccessKey
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.events.Events
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.reverseProxyRouting
import org.http4k.routing.routes
import java.time.Clock

fun Cluster(
    events: Events = ::println,
    theInternet: RoutingHttpHandler,
    clock: Clock = Clock.systemUTC()
): HttpHandler {
    val env = Environment.defaults(
        AWS_REGION of EU_WEST_1,
        AWS_ACCESS_KEY_ID of AccessKeyId.of("access-key-id"),
        AWS_SECRET_ACCESS_KEY of SecretAccessKey.of("secret-access-key"),
        DISPATCH_QUEUE of Uri.of("http://aws/DISPATCH_QUEUE"),
        DATABASE_URL of "jdbc:h2:mem:warehouse;DB_CLOSE_DELAY=-1",
        DATABASE_DRIVER of "org.h2.Driver",
        NOTIFICATION_TOPIC_ARN of ARN.parse("arn:aws:sns:us-east-2:123456789012:MyExampleTopic"),
    )

    setupCloudResources(env, theInternet)

    val networkAccess = NetworkAccess()

    val internalHttp = reverseProxyRouting(
        "auth" to Auth(env, events, clock, networkAccess),
        "warehouse" to Warehouse(env, events, clock, networkAccess, Inventory.Fake(events, clock)),
        "website" to Website(env, events, clock, networkAccess)
    )

    networkAccess.http = routes(internalHttp, theInternet)

    return ApiGateway(env, events, clock, networkAccess)
}
