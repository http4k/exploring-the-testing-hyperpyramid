import hyperpyramid.dto.ItemId
import org.http4k.core.Uri

interface Event

data class HttpCall(val uri: Uri) : Event

typealias Events = (Event) -> Unit

fun main() {
    Customer(TODO(), TODO(), TODO(), TODO()).listItems()
    Customer(TODO(), TODO(), TODO(), TODO()).order(ItemId.of("!23"))

    val a: Events = TODO()
    a(HttpCall(Uri.of("asdsd")))
    val b: HttpHandler = TODO()
}
