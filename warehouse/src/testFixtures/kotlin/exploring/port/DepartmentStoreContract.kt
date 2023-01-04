package exploring.port

import dev.forkhandles.result4k.orThrow
import exploring.dto.Email
import exploring.dto.ItemId
import exploring.dto.Order
import exploring.dto.OrderId
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNull

interface DepartmentStoreContract {
    val departmentStore: DepartmentStore

    @Test
    fun `can store and get orders`() {
        val email = Email.of("01234567890")
        val id = ItemId.of("123")
        val orderId = departmentStore.collection(email, id).orThrow()

        expectThat(departmentStore.lookup(orderId).orThrow()).isEqualTo(Order(email, listOf(id)))
    }

    @Test
    fun `non existent order`() {
        expectThat(departmentStore.lookup(OrderId.of(999)).orThrow()).isNull()
    }
}
