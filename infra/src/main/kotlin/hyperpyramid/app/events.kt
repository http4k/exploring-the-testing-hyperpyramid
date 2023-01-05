package hyperpyramid.app

import hyperpyramid.dto.ItemId
import org.http4k.events.Event

data class DbCall(val name: String) : Event

interface BusinessEvent : Event

data class CustomerOrder(val item: ItemId) : BusinessEvent
