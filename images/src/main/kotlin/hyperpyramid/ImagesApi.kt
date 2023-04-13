package hyperpyramid

import hyperpyramid.ImageSettings.IMAGE_BUCKET
import hyperpyramid.app.AppEvents
import hyperpyramid.app.AppIncomingHttp
import hyperpyramid.app.AppOutgoingHttp
import hyperpyramid.endpoint.GetImage
import hyperpyramid.port.Images
import org.http4k.cloudnative.env.Environment
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.s3.Http
import org.http4k.connect.amazon.s3.S3Bucket
import org.http4k.core.HttpHandler
import org.http4k.events.Events
import java.time.Clock

fun ImagesApi(
    env: Environment,
    clock: Clock,
    events: Events,
    http: HttpHandler
): HttpHandler {
    val appEvents = AppEvents("images", clock, events)
    val outgoingHttp = AppOutgoingHttp(appEvents, http)

    return AppIncomingHttp(
        appEvents,
        GetImage(Images(S3Bucket.Http(env[IMAGE_BUCKET], env[AWS_REGION], env, outgoingHttp)))
    )
}

