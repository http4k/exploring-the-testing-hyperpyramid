package hyperpyramid.scenarios

import hyperpyramid.actors.StoreManager
import hyperpyramid.actors.WebsiteCustomer
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isTrue

interface ItemTransactionScenario {
    val storeManager: StoreManager
    val customer: WebsiteCustomer

    @Test
    fun `can load stock list and order item`() {
        with(customer) {
            login()

            val catalogue = listItems()

            val itemId = catalogue.first()
            expectThat(canSeeImage(itemId)).isTrue()

            val orderId = order(itemId)
            expectThat(storeManager.hasOrderItems(orderId)).isEqualTo(listOf(itemId))

            expectThat(customer.hasEmailFor(orderId)).isTrue()
        }
    }
}
