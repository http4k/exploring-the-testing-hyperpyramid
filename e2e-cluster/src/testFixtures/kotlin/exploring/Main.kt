package exploring

import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun main() {
    Cluster(theInternet = TheInternet()).asServer(SunHttp(8000)).start()
}
