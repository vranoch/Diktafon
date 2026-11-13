package app.diktafon.di

import app.diktafon.structuring.data.JsonSchemaDocumentValidator
import app.diktafon.structuring.data.MockDocumentProcessor
import app.diktafon.structuring.data.StructuredDocumentRepositoryImpl
import app.diktafon.structuring.domain.DocumentProcessor
import app.diktafon.structuring.domain.StructuredDocumentRepository
import app.diktafon.structuring.domain.StructuredDocumentValidator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class StructuringBindingsModule {

    @Binds
    abstract fun bindDocumentProcessor(impl: MockDocumentProcessor): DocumentProcessor

    @Binds
    abstract fun bindStructuredDocumentValidator(impl: JsonSchemaDocumentValidator): StructuredDocumentValidator

    @Binds
    abstract fun bindStructuredDocumentRepository(impl: StructuredDocumentRepositoryImpl): StructuredDocumentRepository
}
