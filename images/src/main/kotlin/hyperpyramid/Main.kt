package hyperpyramid

import org.http4k.server.Undertow
import org.http4k.server.asServer

fun main() {
    ImagesApi().asServer(Undertow(8000)).start()
}

