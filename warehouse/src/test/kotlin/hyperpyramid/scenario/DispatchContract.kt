package hyperpyramid.scenario

import hyperpyramid.dto.ItemId
import org.http4k.core.Status
import org.http4k.strikt.status
import org.http4k.testing.Approver
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

interface DispatchContract : WarehouseContract {

    @Test
    fun `dispatch item`(approver: Approver) {
        approver.assertApproved(client().dispatchAn(ItemId.of("1")))
    }

    @Test
    fun `unknown item`() {
        expectThat(client().dispatchAn(ItemId.of("999"))).status.isEqualTo(Status.NOT_FOUND)
    }

    @Test
    fun `no stock`() {
        expectThat(client().dispatchAn(ItemId.of("2"))).status.isEqualTo(Status.PRECONDITION_FAILED)
    }

}
