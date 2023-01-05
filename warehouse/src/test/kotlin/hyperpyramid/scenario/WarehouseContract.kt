package hyperpyramid.scenario

import hyperpyramid.actor.WarehouseClient
import org.http4k.core.HttpHandler

interface WarehouseContract {
    val http: HttpHandler

    fun client() = WarehouseClient(http)
}
