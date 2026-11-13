package app.diktafon.export.data

import android.content.Intent
import javax.inject.Inject

class AndroidShareIntentFactory @Inject constructor() : ShareIntentFactory {

    override fun createShareTextIntent(content: String, recipientEmails: List<String>): Intent {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, content)
            if (recipientEmails.isNotEmpty()) {
                putExtra(Intent.EXTRA_EMAIL, recipientEmails.toTypedArray())
            }
        }
        return Intent.createChooser(sendIntent, null)
    }
}
