package tests

import apigateway.ApiGatewaySettings.SHOP_URL
import env.TestClock
import env.TheInternet
import hyperpyramid.DbTracer
import hyperpyramid.PumlSequenceDiagram
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
import org.http4k.tracing.tracer.HttpTracer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import shop.ShopApi
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

interface ListItemsScenario {
    val customer: Customer
    @Test
    fun `can list items`() {
        expectThat(customer.listItems()).isEqualTo(listOf(ItemId.of("foo")))
    }
}

class ShopApiTest : RecordTraces(), ListItemsScenario {
    val http: HttpHandler = ShopApi(ShopTestEnv, TestClock, events, FakeWarehouse())

    override val customer = HttpCustomer(Uri.of("http://shop"), TestClock, events, http)
}

class UniverseTest : RecordTraces() {
    val clock = TestClock
    val theInternet = TheInternet()
    val env = ClusterTestEnv
    val system = EcommerceSystem(env, clock, events, theInternet)
    val customer = HttpCustomer(env[SHOP_URL], clock, events, system)

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
