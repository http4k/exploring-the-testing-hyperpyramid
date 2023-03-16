import java.time.Clock
import java.time.Instant
import java.time.ZoneId

object TestClock : Clock() {
    override fun instant(): Instant {
        TODO("Not yet implemented")
    }

    override fun withZone(zone: ZoneId?): Clock {
        TODO("Not yet implemented")
    }

    override fun getZone(): ZoneId {
        TODO("Not yet implemented")
    }
}
