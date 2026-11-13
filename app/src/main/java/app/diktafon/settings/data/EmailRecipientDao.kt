package app.diktafon.settings.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EmailRecipientDao {
    @Insert
    suspend fun insert(entity: EmailRecipientEntity): Long

    @Query("UPDATE email_recipients SET email = :newEmail WHERE id = :id")
    suspend fun update(id: Long, newEmail: String)

    @Query("DELETE FROM email_recipients WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM email_recipients ORDER BY email ASC")
    fun observeAll(): Flow<List<EmailRecipientEntity>>
}
