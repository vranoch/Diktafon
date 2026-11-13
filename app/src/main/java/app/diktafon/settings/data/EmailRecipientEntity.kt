package app.diktafon.settings.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "email_recipients")
data class EmailRecipientEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val email: String,
)
