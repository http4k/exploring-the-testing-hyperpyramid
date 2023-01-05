package hyperpyramid.scenario

import hyperpyramid.actor.WarehouseClient

interface WarehouseContract {
    fun client(): WarehouseClient
}
