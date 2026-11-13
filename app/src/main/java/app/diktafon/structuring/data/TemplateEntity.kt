package app.diktafon.structuring.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "templates")
data class TemplateEntity(
    @PrimaryKey(autoGenerate = true) val rowId: Long = 0,
    val templateId: Long,
    val name: String,
    val version: Int,
    val fieldsJson: String,
    val instructions: String,
    val exampleText: String,
    val createdAtMillis: Long,
)
