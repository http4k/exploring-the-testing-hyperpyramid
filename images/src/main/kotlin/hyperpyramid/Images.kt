package hyperpyramid

import hyperpyramid.ImageSettings.DEBUG
import hyperpyramid.ImageSettings.IMAGE_BUCKET
import hyperpyramid.app.AppEvents
import hyperpyramid.app.AppIncomingHttp
import hyperpyramid.app.AppOutgoingHttp
import hyperpyramid.endpoint.GetImage
import hyperpyramid.port.Images
import hyperpyramid.util.Json
import org.http4k.client.JavaHttpClient
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.Environment.Companion.ENV
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.s3.Http
import org.http4k.connect.amazon.s3.S3Bucket
import org.http4k.core.HttpHandler
import org.http4k.events.AutoMarshallingEvents
import org.http4k.events.Events
import org.http4k.routing.RoutingHttpHandler
import java.time.Clock
import java.time.Clock.systemUTC

fun ImagesApi(
    env: Environment = ENV,
    events: Events = AutoMarshallingEvents(Json),
    clock: Clock = systemUTC(),
    http: HttpHandler = JavaHttpClient()
): RoutingHttpHandler {
    val appEvents = AppEvents("images", clock, events)
    val outgoingHttp = AppOutgoingHttp(env[DEBUG], appEvents, http)

    return AppIncomingHttp(
        env[DEBUG],
        appEvents,
        GetImage(Images(S3Bucket.Http(env[IMAGE_BUCKET], env[AWS_REGION], env, outgoingHttp)))
    )
}

