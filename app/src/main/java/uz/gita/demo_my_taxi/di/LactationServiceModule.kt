package uz.gita.demo_my_taxi.di

import android.content.Context
import uz.gita.demo_my_taxi.service.LocationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.gita.demo_my_taxi.service.impl.LocationServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LactationServiceModule {

    @[Provides Singleton]
    fun getService(@ApplicationContext context: Context): LocationService {
        return LocationServiceImpl(context)
    }

}