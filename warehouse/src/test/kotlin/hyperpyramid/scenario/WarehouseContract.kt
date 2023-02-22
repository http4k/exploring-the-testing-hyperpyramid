package hyperpyramid.scenario

import hyperpyramid.actor.WarehouseClient
import org.http4k.core.HttpHandler
import org.http4k.events.Events

interface WarehouseContract {
    val http: HttpHandler
    val events: Events

    fun client() = WarehouseClient(http, events)
}
