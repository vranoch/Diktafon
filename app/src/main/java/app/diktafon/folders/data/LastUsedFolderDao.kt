package app.diktafon.folders.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface LastUsedFolderDao {
    @Upsert
    suspend fun upsert(entity: LastUsedFolderEntity)

    @Query("SELECT * FROM last_used_folder WHERE id = :id")
    fun observe(id: Int = LastUsedFolderEntity.SINGLETON_ID): Flow<LastUsedFolderEntity?>
}
