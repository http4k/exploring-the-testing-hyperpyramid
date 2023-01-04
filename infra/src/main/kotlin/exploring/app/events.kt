package exploring.app

import org.http4k.events.Event

data class DbCall(val name: String) : Event

data class BusinessEvent(val name: String) : Event
