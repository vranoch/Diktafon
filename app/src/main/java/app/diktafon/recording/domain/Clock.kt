package app.diktafon.recording.domain

interface Clock {
    fun nowMillis(): Long
}
