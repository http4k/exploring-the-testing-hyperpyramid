package hyperpyramid

import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun main() {
    ApiGateway().asServer(SunHttp(8000)).start()
}
