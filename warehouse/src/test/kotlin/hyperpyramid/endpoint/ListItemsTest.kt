package hyperpyramid.endpoint

import org.http4k.testing.Approver
import org.http4k.testing.JsonApprovalTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(JsonApprovalTest::class)
class ListItemsTest : WarehouseTest() {
    @Test
    fun `list items only lists items that are in stock`(approver: Approver) {
        approver.assertApproved(client.listItems())
    }
}

