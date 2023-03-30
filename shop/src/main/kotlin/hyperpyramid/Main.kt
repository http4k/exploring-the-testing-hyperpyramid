package hyperpyramid

import org.http4k.server.Undertow
import org.http4k.server.asServer

fun main() {
    ShopApi().asServer(Undertow(8000)).start()
}

