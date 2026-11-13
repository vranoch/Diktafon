package app.diktafon.structuring.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TemplateDao {
    @Insert
    suspend fun insert(entity: TemplateEntity): Long

    @Query("SELECT COALESCE(MAX(templateId), 0) + 1 FROM templates")
    suspend fun nextTemplateId(): Long

    @Query("SELECT COALESCE(MAX(version), 0) FROM templates WHERE templateId = :templateId")
    suspend fun maxVersion(templateId: Long): Int

    @Query(
        "SELECT * FROM templates WHERE version = (SELECT MAX(version) FROM templates t2 WHERE t2.templateId = templates.templateId) " +
            "ORDER BY name ASC",
    )
    fun observeApprovedCatalog(): Flow<List<TemplateEntity>>
}
