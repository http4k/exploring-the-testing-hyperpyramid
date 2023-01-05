package hyperpyramid.endpoint

import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.testing.Approver
import org.http4k.testing.JsonApprovalTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(JsonApprovalTest::class)
class ListItemsTest : WarehouseTest() {
    @Test
    fun `list items`(approver: Approver) {
        approver.assertApproved(http(Request(GET, "/v1/items")))
    }
}
