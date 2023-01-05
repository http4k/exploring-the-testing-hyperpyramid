package hyperpyramid

import org.http4k.server.Undertow
import org.http4k.server.asServer

fun main() {
    ApiGateway().asServer(Undertow(8000)).start()
}
