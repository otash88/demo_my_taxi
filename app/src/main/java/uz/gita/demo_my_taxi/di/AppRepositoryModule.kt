package uz.gita.demo_my_taxi.di


import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.demo_my_taxi.domain.AppRepository
import uz.gita.demo_my_taxi.domain.LocationRepository
import uz.gita.demo_my_taxi.data.repository.AppRepositoryImpl
import uz.gita.demo_my_taxi.data.repository.LocationRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
interface AppRepositoryModule {

    @Binds
    fun getRepository(impl: AppRepositoryImpl): AppRepository

    @Binds
    fun getLocationRepository(impl: LocationRepositoryImpl): LocationRepository

}
