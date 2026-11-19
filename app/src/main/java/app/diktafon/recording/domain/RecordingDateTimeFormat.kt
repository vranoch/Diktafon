package app.diktafon.recording.domain

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val RECORDING_DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("d. M. yyyy, H:mm")

/** Sdilene formatovani data/casu nahravky - pouzivano pro automaticky doplnovana pole sablony
 * (viz FR-TPL-007) i pro predmet e-mailu pri exportu. */
fun formatRecordingDateTime(epochMillis: Long): String =
    Instant.ofEpochMilli(epochMillis).atZone(ZoneId.systemDefault()).format(RECORDING_DATE_TIME_FORMATTER)
