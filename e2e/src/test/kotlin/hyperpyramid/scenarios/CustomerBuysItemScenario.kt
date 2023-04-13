package hyperpyramid.scenarios

import hyperpyramid.actors.StoreManager
import hyperpyramid.actors.WebsiteCustomer
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isTrue

interface CustomerBuysItemScenario {
    val storeManager: StoreManager
    val customer: WebsiteCustomer

    @Test
    fun `views item, orders, receives confirmation`() {
        val itemId = customer.listItems().first()
        expectThat(customer.canSeeImage(itemId)).isTrue()

        val orderId = customer.order(itemId, "joe@kotlin.org")
        expectThat(customer.hasEmailFor(orderId, "joe@kotlin.org")).isTrue()
        expectThat(storeManager.hasOrderItems(orderId)).isEqualTo(listOf(itemId))
    }
}
