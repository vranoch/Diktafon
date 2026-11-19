package app.diktafon.recording.domain

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime

class RecordingDateTimeFormatTest {

    @Test
    fun `naformatuje epoch millis jako den mesic rok a cas`() {
        val epochMillis = ZonedDateTime.of(2026, 11, 19, 8, 5, 0, 0, ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        assertEquals("19. 11. 2026, 8:05", formatRecordingDateTime(epochMillis))
    }
}
