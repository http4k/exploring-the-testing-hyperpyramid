package exploring

import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun main() {
    Warehouse().asServer(SunHttp(8000)).start()
}

