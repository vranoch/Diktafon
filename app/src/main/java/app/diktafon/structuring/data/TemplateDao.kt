package app.diktafon.structuring.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TemplateDao {
    @Insert
    suspend fun insert(entity: TemplateEntity): Long

    @Query("DELETE FROM templates WHERE templateId = :templateId")
    suspend fun deleteAllVersions(templateId: Long)

    @Query("DELETE FROM templates WHERE templateId = :templateId AND version = :version")
    suspend fun deleteVersion(templateId: Long, version: Int)

    @Query(
        "UPDATE templates SET name = :name, fieldsJson = :fieldsJson, instructions = :instructions, " +
            "exampleText = :exampleText WHERE templateId = :templateId AND version = :version",
    )
    suspend fun updateVersion(templateId: Long, version: Int, name: String, fieldsJson: String, instructions: String, exampleText: String)

    @Query("SELECT * FROM templates WHERE templateId = :templateId ORDER BY version DESC")
    fun observeVersions(templateId: Long): Flow<List<TemplateEntity>>

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
