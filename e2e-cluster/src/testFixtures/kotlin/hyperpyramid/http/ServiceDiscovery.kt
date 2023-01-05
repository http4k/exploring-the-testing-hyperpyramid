package hyperpyramid.http

import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.server.SunHttp
import org.http4k.server.asServer

/**
 * Provider
 */
fun interface ServiceDiscovery : (String) -> Uri

fun LocalhostServiceDiscovery(startingPort: Int, vararg overrides: String): ServiceDiscovery {
    var port = startingPort
    return ServiceDiscovery(overrides.map { it to Uri.of("http://localhost:${port++}") }.toList())
}

fun ServiceDiscovery(overrides: List<Pair<String, Uri>> = emptyList()) = ServiceDiscovery { service ->
    overrides.firstOrNull { it.first == service }?.second ?: Uri.of("http://$service")
}

fun HttpHandler.start(serviceDiscovery: ServiceDiscovery, name: String) {
    asServer(SunHttp(serviceDiscovery(name).port!!)).start()
    println(name + " started at " + serviceDiscovery(name))
}
