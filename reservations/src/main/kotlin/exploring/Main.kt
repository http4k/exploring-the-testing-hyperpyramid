package exploring

import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun main() {
    Reservations().asServer(SunHttp(8000)).start()
}
