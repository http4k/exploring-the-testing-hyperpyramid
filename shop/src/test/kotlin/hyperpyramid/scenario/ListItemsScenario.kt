package hyperpyramid.scenario

import hyperpyramid.actors.Customer
import hyperpyramid.dto.ItemId
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

interface ListItemsScenario {
    val customer: Customer
    @Test
    fun `can list items`() {
        expectThat(customer.listItems()).isEqualTo(listOf(ItemId.of("1")))
    }
}
