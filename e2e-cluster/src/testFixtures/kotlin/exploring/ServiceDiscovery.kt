package exploring

import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.server.SunHttp
import org.http4k.server.asServer

typealias ServiceDiscovery = (String) -> Uri

fun ServiceDiscovery(vararg overrides: Pair<String, Uri>) = ServiceDiscovery(overrides.toList())

fun LocalhostServiceDiscovery(startingPort: Int, vararg overrides: String): ServiceDiscovery {
    var port = startingPort
    return ServiceDiscovery(overrides.map { it to Uri.of("http://localhost:${port++}") }.toList())
}

private fun ServiceDiscovery(overrides: List<Pair<String, Uri>>): ServiceDiscovery = { service ->
    overrides.firstOrNull { it.first == service }?.second ?: Uri.of("http://$service")
}

fun HttpHandler.start(serviceDiscovery: ServiceDiscovery, name: String) {
    asServer(SunHttp(serviceDiscovery(name).port!!)).start()
    println(name + " started at " + serviceDiscovery(name))
}
