package hyperpyramid.scenario

import hyperpyramid.dto.ItemId
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

interface BrowsingContract : ShopContract {
    @Test
    fun `can list items`() {
        with(client()) {
            expectThat(listItems()).isEqualTo(listOf(ItemId.of("1")))
        }
    }
}
