package app.diktafon.settings.data

import app.diktafon.settings.domain.TranscriptionLanguage
import app.diktafon.settings.domain.TranscriptionLanguageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TranscriptionLanguageRepositoryImpl @Inject constructor(
    private val dao: TranscriptionLanguageDao,
) : TranscriptionLanguageRepository {

    override suspend fun add(code: String, displayName: String) {
        dao.insert(TranscriptionLanguageEntity(code = code, displayName = displayName))
    }

    override suspend fun remove(code: String) = dao.delete(code)

    override fun observeAll(): Flow<List<TranscriptionLanguage>> =
        dao.observeAll().map { entities -> entities.map { TranscriptionLanguage(it.code, it.displayName) } }
}
