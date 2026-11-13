package app.diktafon.recording.domain

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class NameGenerator @Inject constructor(private val clock: Clock) {

    fun generatePregeneratedName(): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        return "Nahrávka ${format.format(Date(clock.nowMillis()))}"
    }
}
