package hyperpyramid

import org.http4k.events.MetadataEvent
import org.http4k.tracing.Actor
import org.http4k.tracing.ActorResolver
import org.http4k.tracing.ActorType.System
import org.http4k.tracing.TraceRenderPersistence
import org.http4k.tracing.junit.TracerBulletEvents
import org.http4k.tracing.persistence.FileSystem
import org.http4k.tracing.renderer.PumlInteractionDiagram
import org.http4k.tracing.tracer.HttpTracer
import org.junit.jupiter.api.extension.RegisterExtension
import java.io.File

abstract class TracingTest {
    @RegisterExtension
    val events = TracerBulletEvents(
        listOf(::HttpTracer, ::DbTracer, ::BusinessEventTracer).map { it(ActorByService) },
        listOf(PumlSequenceDiagram, PumlInteractionDiagram),
        TraceRenderPersistence.FileSystem(File(".generated"))
    )
}

object ActorByService : ActorResolver {
    override fun invoke(it: MetadataEvent) = Actor(
        it.metadata["service"]!!.toString(), System
    )
}
