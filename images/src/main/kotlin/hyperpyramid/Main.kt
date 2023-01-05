package hyperpyramid

import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun main() {
    Images().asServer(SunHttp(8000)).start()
}

