package app.diktafon.settings.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "provider_config")
data class ProviderConfigEntity(
    @PrimaryKey val id: Int = SINGLETON_ID,
    val sttProviderName: String,
    val sttModel: String,
    val llmProviderName: String,
    val llmModel: String,
    val defaultTemplateId: Long?,
    val defaultLanguageCode: String,
    val importAudioOnlyFilter: Boolean = false,
) {
    companion object {
        const val SINGLETON_ID = 0
    }
}
