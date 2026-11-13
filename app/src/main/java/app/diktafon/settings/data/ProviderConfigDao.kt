package app.diktafon.settings.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ProviderConfigDao {
    @Upsert
    suspend fun upsert(entity: ProviderConfigEntity)

    @Query("SELECT * FROM provider_config WHERE id = :id")
    fun observe(id: Int = ProviderConfigEntity.SINGLETON_ID): Flow<ProviderConfigEntity?>
}
