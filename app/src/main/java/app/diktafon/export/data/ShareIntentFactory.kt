package app.diktafon.export.data

import android.content.Intent

interface ShareIntentFactory {
    fun createShareTextIntent(content: String, recipientEmails: List<String> = emptyList(), subject: String = ""): Intent
}
