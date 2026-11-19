package app.diktafon.di

import app.diktafon.structuring.data.TemplateNameRecognizerSelector
import app.diktafon.structuring.domain.TemplateNameRecognizer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TemplateSuggestionBindingsModule {

    @Binds
    abstract fun bindTemplateNameRecognizer(impl: TemplateNameRecognizerSelector): TemplateNameRecognizer
}
