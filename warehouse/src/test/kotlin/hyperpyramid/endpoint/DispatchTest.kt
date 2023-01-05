package hyperpyramid.endpoint

import hyperpyramid.dto.ItemId
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.PRECONDITION_FAILED
import org.http4k.strikt.status
import org.http4k.testing.ApprovalTest
import org.http4k.testing.Approver
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@ExtendWith(ApprovalTest::class)
class DispatchTest : WarehouseTest() {

    @Test
    fun `dispatch item`(approver: Approver) {
        approver.assertApproved(client.dispatchA(ItemId.of("1")))
    }

    @Test
    fun `unknown item`() {
        expectThat(client.dispatchA(ItemId.of("999"))).status.isEqualTo(NOT_FOUND)
    }

    @Test
    fun `no stock`() {
        expectThat(client.dispatchA(ItemId.of("2"))).status.isEqualTo(PRECONDITION_FAILED)
    }
}
