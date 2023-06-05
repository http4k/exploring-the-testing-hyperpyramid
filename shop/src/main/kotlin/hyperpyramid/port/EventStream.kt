package hyperpyramid.port

import hyperpyramid.app.BusinessEvent

fun interface EventStream {
    fun emit(businessEvent: BusinessEvent)
}
