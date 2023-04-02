package hyperpyramid

import hyperpyramid.util.Json
import org.http4k.client.JavaHttpClient
import org.http4k.cloudnative.env.Environment.Companion.ENV
import org.http4k.events.AutoMarshallingEvents
import org.http4k.server.Undertow
import org.http4k.server.asServer
import java.time.Clock.systemUTC

fun main() {
    ShopApi(ENV, systemUTC(), AutoMarshallingEvents(Json), JavaHttpClient())
        .asServer(Undertow(8000)).start()
}

