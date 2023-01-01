package exploring

import exploring.ClusterSettings.DEV_MODE
import exploring.WarehouseSettings.DISPATCH_QUEUE
import exploring.WarehouseSettings.INVENTORY_DB_TABLE
import exploring.WebsiteSettings.NOTIFICATION_TOPIC_ARN
import org.http4k.cloudnative.env.Environment
import org.http4k.connect.amazon.AWS_ACCESS_KEY_ID
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.AWS_SECRET_ACCESS_KEY
import org.http4k.connect.amazon.core.model.ARN
import org.http4k.connect.amazon.core.model.AccessKeyId
import org.http4k.connect.amazon.core.model.Region.Companion.EU_WEST_1
import org.http4k.connect.amazon.core.model.SecretAccessKey
import org.http4k.connect.amazon.dynamodb.model.TableName
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.events.Events
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.reverseProxyRouting
import org.http4k.routing.routes
import java.time.Clock

fun Cluster(
    events: Events = ::println,
    clock: Clock = Clock.systemUTC(),
    theInternet: RoutingHttpHandler
): HttpHandler {

    val env = Environment.defaults(
        DEV_MODE of false,
        AWS_REGION of EU_WEST_1,
        AWS_ACCESS_KEY_ID of AccessKeyId.of("access-key-id"),
        AWS_SECRET_ACCESS_KEY of SecretAccessKey.of("secret-access-key"),
        INVENTORY_DB_TABLE of TableName.of("inventory"),
        DISPATCH_QUEUE of Uri.of("http://aws/DISPATCH_QUEUE"),
        NOTIFICATION_TOPIC_ARN of ARN.parse("arn:aws:sns:us-east-2:123456789012:MyExampleTopic"),
    )

    setupCloudResources(env, theInternet)

    val networkAccess = NetworkAccess()

    val internalHttp = reverseProxyRouting(
        "auth" to Auth(env, events, clock, networkAccess),
        "warehouse" to Warehouse(env, events, clock, networkAccess),
        "website" to Website(env, events, clock, networkAccess)
    )

    networkAccess.http = routes(internalHttp, theInternet)

    return ApiGateway(env, events, clock, networkAccess)
}
