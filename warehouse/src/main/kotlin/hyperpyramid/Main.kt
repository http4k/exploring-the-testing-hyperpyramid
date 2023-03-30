package hyperpyramid

import org.http4k.server.Undertow
import org.http4k.server.asServer

fun main() {
    WarehouseApi().asServer(Undertow(8000)).start()
}

