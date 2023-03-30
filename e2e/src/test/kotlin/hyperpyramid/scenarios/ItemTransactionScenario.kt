package hyperpyramid.scenarios

import hyperpyramid.TheInternet
import hyperpyramid.actors.Customer
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isTrue

interface ItemTransactionScenario {
    val theInternet: TheInternet
    val customer: Customer

    @Test
    fun `can load stock list and order item`() {
        with(customer) {
            login()

            val catalogue = listItems()

            val itemId = catalogue.first()
            expectThat(canSeeImage(itemId)).isTrue()

            val orderId = order(itemId)
            expectThat(theInternet.departmentStore.orders[orderId]?.items).isEqualTo(listOf(itemId))

            expectThat(customer.hasEmailFor(orderId)).isTrue()
        }
    }
}
