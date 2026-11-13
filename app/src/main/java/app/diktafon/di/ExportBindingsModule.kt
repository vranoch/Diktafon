package app.diktafon.di

import app.diktafon.export.data.AndroidShareIntentFactory
import app.diktafon.export.data.MarkdownDocumentFormatter
import app.diktafon.export.data.PlainTextTemplateFormatter
import app.diktafon.export.data.ShareIntentFactory
import app.diktafon.export.domain.DocumentFormatter
import app.diktafon.export.domain.TemplateFormatter
import app.diktafon.settings.data.EmailRecipientsRepositoryImpl
import app.diktafon.settings.domain.EmailRecipientsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ExportBindingsModule {

    @Binds
    abstract fun bindDocumentFormatter(impl: MarkdownDocumentFormatter): DocumentFormatter

    @Binds
    abstract fun bindTemplateFormatter(impl: PlainTextTemplateFormatter): TemplateFormatter

    @Binds
    abstract fun bindShareIntentFactory(impl: AndroidShareIntentFactory): ShareIntentFactory

    @Binds
    abstract fun bindEmailRecipientsRepository(impl: EmailRecipientsRepositoryImpl): EmailRecipientsRepository
}
