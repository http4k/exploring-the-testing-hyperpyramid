import hyperpyramid.BusinessEventTracer
import hyperpyramid.DbTracer
import hyperpyramid.FakeWarehouse
import hyperpyramid.Website
import hyperpyramid.actors.Customer
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

class BrowsingTest {

    @RegisterExtension
    val events = TracerBulletEvents(
        listOf(::HttpTracer, ::DbTracer, ::BusinessEventTracer).map { it((ActorByService)) },
        listOf(PumlSequenceDiagram, PumlInteractionDiagram),
        TraceRenderPersistence.FileSystem(File(".generated"))
    )

    val http: HttpHandler = Website(WebsiteTestEnv, events, http = FakeWarehouse())

    @Test
    fun `can list items`() {
        with(Customer(http, Uri.of("http://website"), { emptyList() }, events)) {
            expectThat(listItems()).isEqualTo(listOf(ItemId.of("foo")))
        }
    }
}

object ActorByService : ActorResolver {
    override fun invoke(it: MetadataEvent) = Actor(
        it.metadata["service"]!!.toString(), ActorType.System
    )
}

val WebsiteTestEnv = Environment.defaults(
)
