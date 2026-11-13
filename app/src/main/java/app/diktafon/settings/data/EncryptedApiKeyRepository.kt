package app.diktafon.settings.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import app.diktafon.settings.domain.ApiKeyRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

// API klice se nikdy nezapisuji do logu (FR-CFG-001) a jsou chranene pres Android Keystore
// (NFR-SEC-002), ne v APK/repozitari/zalohach. Neni testovatelne pod Robolectric (viz ADR-011) -
// skutecny Keystore vyzaduje realne zarizeni/emulator.
class EncryptedApiKeyRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) : ApiKeyRepository {

    private val masterKey by lazy {
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    private val preferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            "diktafon_api_keys",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    override suspend fun saveApiKey(providerName: String, apiKey: String) {
        preferences.edit().putString(providerName, apiKey).apply()
    }

    override suspend fun getApiKey(providerName: String): String? = preferences.getString(providerName, null)

    override suspend fun clearApiKey(providerName: String) {
        preferences.edit().remove(providerName).apply()
    }

    override suspend fun hasApiKey(providerName: String): Boolean = preferences.contains(providerName)
}
