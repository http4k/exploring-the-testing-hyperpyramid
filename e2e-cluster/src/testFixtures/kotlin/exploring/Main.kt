package exploring

import org.http4k.cloudnative.env.Environment.Companion.ENV
import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun main() {
    Cluster(ENV, theInternet = TheInternet()).asServer(SunHttp(8000)).start()
}
