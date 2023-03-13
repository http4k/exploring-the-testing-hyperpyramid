import hyperpyramid.dto.ItemId
import org.http4k.core.Request
import org.http4k.core.Response

typealias HttpHandler = (Request) -> Response

interface Event

typealias Events = (Event) -> Unit

fun main() {
    Customer(TODO(), TODO(), TODO(), TODO()).listItems()
    Customer(TODO(), TODO(), TODO(), TODO()).order(ItemId.of("!23"))

    val a : Events = TODO()
    val b : HttpHandler = TODO()
}
