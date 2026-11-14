package app.diktafon.settings.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TranscriptionLanguageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: TranscriptionLanguageEntity)

    @Query("DELETE FROM transcription_languages WHERE code = :code")
    suspend fun delete(code: String)

    @Query("SELECT * FROM transcription_languages ORDER BY displayName ASC")
    fun observeAll(): Flow<List<TranscriptionLanguageEntity>>
}
