package hyperpyramid.scenarios

import hyperpyramid.TheInternet
import hyperpyramid.actors.HttpCustomer
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isTrue

interface LoadStockList {
    val theInternet: TheInternet
    val user: HttpCustomer

    @Test
    fun `can load stock list and order item`() {
        with(user) {
            login()

            val catalogue = listItems()

            val itemId = catalogue.first()
            expectThat(canSeeImage(itemId)).isTrue()

            val orderId = order(itemId)
            expectThat(theInternet.departmentStore.orders[orderId]?.items).isEqualTo(listOf(itemId))

            expectThat(user.hasEmailFor(orderId)).isTrue()
        }
    }
}
