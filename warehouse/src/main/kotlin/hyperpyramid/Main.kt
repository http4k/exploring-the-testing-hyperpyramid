package hyperpyramid

import hyperpyramid.adapter.DatabaseInventory
import hyperpyramid.util.Json
import org.http4k.client.JavaHttpClient
import org.http4k.config.Environment.Companion.ENV
import org.http4k.events.AutoMarshallingEvents
import org.http4k.server.Undertow
import org.http4k.server.asServer
import java.time.Clock

fun main() {
    WarehouseApi(
        ENV, Clock.systemUTC(),
        AutoMarshallingEvents(Json), JavaHttpClient(),
        DatabaseInventory(ENV)
    ).asServer(Undertow(8000)).start()
}

