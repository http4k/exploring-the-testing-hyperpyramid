package exploring

import exploring.ImageSettings.DEBUG
import exploring.ImageSettings.IMAGE_BUCKET
import exploring.app.AppEvents
import exploring.app.AppIncomingHttp
import exploring.app.AppOutgoingHttp
import exploring.endpoint.GetImage
import exploring.port.ImagesHub
import org.http4k.client.JavaHttpClient
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.Environment.Companion.ENV
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.s3.Http
import org.http4k.connect.amazon.s3.S3Bucket
import org.http4k.core.HttpHandler
import org.http4k.events.Events
import org.http4k.routing.RoutingHttpHandler
import java.time.Clock
import java.time.Clock.systemUTC

fun Images(
    env: Environment = ENV,
    events: Events = ::println,
    clock: Clock = systemUTC(),
    http: HttpHandler = JavaHttpClient()
): RoutingHttpHandler {
    val appEvents = AppEvents("images", clock, events)
    val outgoingHttp = AppOutgoingHttp(DEBUG(env), appEvents, http)

    return AppIncomingHttp(
        DEBUG(env),
        appEvents,
        GetImage(ImagesHub(S3Bucket.Http(IMAGE_BUCKET(env), AWS_REGION(env), env, outgoingHttp)))
    )
}

