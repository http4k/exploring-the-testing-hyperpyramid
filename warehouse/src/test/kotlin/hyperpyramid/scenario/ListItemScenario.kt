package hyperpyramid.scenario

import org.http4k.testing.Approver
import org.junit.jupiter.api.Test

interface ListItemScenario : WarehouseContract {

    @Test
    fun `list items only lists items that are in stock`(approver: Approver) {
        approver.assertApproved(client().listItems())
    }
}
