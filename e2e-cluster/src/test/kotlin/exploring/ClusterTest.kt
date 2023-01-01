package exploring

import exploring.actors.WebsiteUser
import org.http4k.core.Uri
import org.http4k.events.then
import org.junit.jupiter.api.Test

class ClusterTest : TracingTest() {
    private val cluster = Cluster(events.then(::println), FakeAws())

    private val user = WebsiteUser(events, cluster, Uri.of("http://api-gateway"))

    @Test
    fun `can load stock list`() {
        user.listItems()
    }
}
