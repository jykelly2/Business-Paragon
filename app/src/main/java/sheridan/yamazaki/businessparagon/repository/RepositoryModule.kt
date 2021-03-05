package sheridan.yamazaki.businessparagon.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import sheridan.yamazaki.businessparagon.model.Business

@Module
@InstallIn(ApplicationComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindBusinessRepository(
            repository: BusinessRepositoryImpl): BusinessRepository

}