package app.diktafon.folders.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {
    @Insert
    suspend fun insert(entity: FolderEntity): Long

    @Query("UPDATE folders SET name = :newName WHERE id = :id")
    suspend fun rename(id: Long, newName: String)

    @Query("DELETE FROM folders WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("UPDATE folders SET parentFolderId = :newParentFolderId WHERE id = :id")
    suspend fun move(id: Long, newParentFolderId: Long?)

    @Query(
        "SELECT * FROM folders WHERE (:parentFolderId IS NULL AND parentFolderId IS NULL) OR parentFolderId = :parentFolderId " +
            "ORDER BY name ASC",
    )
    fun observeChildren(parentFolderId: Long?): Flow<List<FolderEntity>>

    @Query("SELECT COUNT(*) FROM folders WHERE parentFolderId = :id")
    suspend fun countChildren(id: Long): Int

    @Query("SELECT * FROM folders WHERE id = :id")
    fun observeById(id: Long): Flow<FolderEntity?>
}
