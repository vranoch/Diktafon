package app.diktafon.folders.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "last_used_folder")
data class LastUsedFolderEntity(
    @PrimaryKey val id: Int = SINGLETON_ID,
    val folderId: Long?,
) {
    companion object {
        const val SINGLETON_ID = 0
    }
}
