package exploring

import exploring.actors.WebsiteUser
import org.http4k.core.Uri
import org.http4k.events.then
import org.junit.jupiter.api.Test

class ClusterTest : TracingTest() {
    val events1 = events.then(::println)
    private val cluster = Cluster(
        events1, FakeAws()
    )

    private val user = WebsiteUser(events1, cluster, Uri.of("http://api-gateway"))

    @Test
    fun `can load stock list`() {
        user.listItems()
    }
}
