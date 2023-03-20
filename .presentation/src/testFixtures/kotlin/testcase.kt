import hyperpyramid.DbTracer
import hyperpyramid.dto.ItemId
import org.http4k.cloudnative.env.Environment
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.events.MetadataEvent
import org.http4k.tracing.Actor
import org.http4k.tracing.ActorResolver
import org.http4k.tracing.ActorType
import org.http4k.tracing.TraceRenderPersistence
import org.http4k.tracing.junit.TracerBulletEvents
import org.http4k.tracing.persistence.FileSystem
import org.http4k.tracing.renderer.PumlInteractionDiagram
import org.http4k.tracing.renderer.PumlSequenceDiagram
import org.http4k.tracing.tracer.HttpTracer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.io.File

abstract class RecordTraces {
    @RegisterExtension
    val events = TracerBulletEvents(
        listOf(::HttpTracer, ::DbTracer).map { it(ActorByService) },
        listOf(PumlSequenceDiagram, PumlInteractionDiagram),
        TraceRenderPersistence.FileSystem(File(".generated"))
    )
}

class ShopApiTest : RecordTraces() {
    val http: HttpHandler = ShopApi(ShopTestEnv, TestClock, events, FakeWarehouse())

    val customer = HttpCustomer(Uri.of("http://shop"), TestClock, events, http)

    @Test
    fun `can list items`() {
        expectThat(customer.listItems()).isEqualTo(listOf(ItemId.of("foo")))
    }
}

class NamespaceTest : RecordTraces() {
    val clock = TestClock
    val theInternet = TheInternet()
    val namespace = Namespace(ClusterTestEnv, clock, events, theInternet)
    val customer = HttpCustomer(Uri.of("http://shop"), clock, events, namespace)

    @Test
    fun `can load stock list and order item`() {
        val itemId = customer.listItems().first()

        val order = customer.order(itemId)

        expectThat(theInternet.departmentStore.orders[order]?.items)
            .isEqualTo(listOf(itemId))
    }
}

object ActorByService : ActorResolver {
    override fun invoke(it: MetadataEvent) = Actor(
        it.metadata["service"]!!.toString(), ActorType.System
    )
}

val ClusterTestEnv = Environment.defaults(
)
val ShopTestEnv = Environment.defaults(
)
