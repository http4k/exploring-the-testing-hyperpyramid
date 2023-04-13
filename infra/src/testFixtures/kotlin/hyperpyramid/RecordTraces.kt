package hyperpyramid

import org.http4k.tracing.Actor
import org.http4k.tracing.ActorResolver
import org.http4k.tracing.ActorType.System
import org.http4k.tracing.TraceRenderPersistence
import org.http4k.tracing.junit.ReportingMode.Companion.Always
import org.http4k.tracing.junit.TracerBulletEvents
import org.http4k.tracing.persistence.FileSystem
import org.http4k.tracing.renderer.PumlInteractionDiagram
import org.http4k.tracing.renderer.PumlSequenceDiagram
import org.http4k.tracing.tracer.HttpTracer
import org.junit.jupiter.api.extension.RegisterExtension
import java.io.File

abstract class RecordTraces {
    @RegisterExtension
    val events = TracerBulletEvents(
        listOf(::HttpTracer, ::DbTracer, ::BusinessEventTracer).map { it(ActorByService) },
        listOf(PumlSequenceDiagram, PumlInteractionDiagram),
        TraceRenderPersistence.FileSystem(File(".generated")),
        reportingMode = Always
    )
}

val ActorByService = ActorResolver {
    Actor(it.metadata["service"]!!.toString(), System)
}
