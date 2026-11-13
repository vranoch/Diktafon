package app.diktafon.settings.data

import app.diktafon.settings.domain.EmailRecipient
import app.diktafon.settings.domain.EmailRecipientsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EmailRecipientsRepositoryImpl @Inject constructor(
    private val dao: EmailRecipientDao,
) : EmailRecipientsRepository {

    override suspend fun add(email: String): Long = dao.insert(EmailRecipientEntity(email = email))

    override suspend fun update(id: Long, newEmail: String) = dao.update(id, newEmail)

    override suspend fun remove(id: Long) = dao.delete(id)

    override fun observeAll(): Flow<List<EmailRecipient>> =
        dao.observeAll().map { entities -> entities.map { EmailRecipient(it.id, it.email) } }
}
