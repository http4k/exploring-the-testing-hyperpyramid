package hyperpyramid

import hyperpyramid.dto.InventoryItem
import hyperpyramid.dto.ItemId
import hyperpyramid.util.Json.auto
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with

class FakeWarehouse : HttpHandler {
    override fun invoke(p1: Request) = Response(OK)
        .with(
            Body.auto<List<InventoryItem>>().toLens() of listOf(
                InventoryItem(ItemId.of("foo"), "bar", 1)
            )
        )
}
