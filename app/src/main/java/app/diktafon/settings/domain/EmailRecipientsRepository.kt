package app.diktafon.settings.domain

import kotlinx.coroutines.flow.Flow

interface EmailRecipientsRepository {
    suspend fun add(email: String): Long
    suspend fun update(id: Long, newEmail: String)
    suspend fun remove(id: Long)
    fun observeAll(): Flow<List<EmailRecipient>>
}
