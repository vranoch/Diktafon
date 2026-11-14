package app.diktafon.settings.domain

import kotlinx.coroutines.flow.Flow

interface TranscriptionLanguageRepository {
    suspend fun add(code: String, displayName: String)
    suspend fun remove(code: String)
    fun observeAll(): Flow<List<TranscriptionLanguage>>
}
