package exploring

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.events.MetadataEvent
import org.http4k.events.then
import org.http4k.tracing.Actor
import org.http4k.tracing.ActorResolver
import org.http4k.tracing.ActorType
import org.http4k.tracing.TraceRenderPersistence
import org.http4k.tracing.junit.TracerBulletEvents
import org.http4k.tracing.persistence.FileSystem
import org.http4k.tracing.renderer.PumlInteractionDiagram
import org.http4k.tracing.tracer.HttpTracer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.io.File

class ClusterTest {

    @RegisterExtension
    val events = TracerBulletEvents(
        listOf(HttpTracer(ServiceName())),
        listOf(PumlInteractionDiagram),
        TraceRenderPersistence.FileSystem(File(".generated"))
    )

    val cluster = Cluster(events.then(::println), FakeAws())

    @Test
    fun `can load stock list`() {
        cluster(Request(Method.GET, "/"))
    }
}

class ServiceName : ActorResolver {
    override fun invoke(it: MetadataEvent) = Actor(it.metadata["service"]!!.toString(), ActorType.System)
}
