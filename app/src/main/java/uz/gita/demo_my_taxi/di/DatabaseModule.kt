package uz.gita.demo_my_taxi.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.gita.demo_my_taxi.data.local.dao.LocationDao
import uz.gita.demo_my_taxi.data.local.db.MyDB
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @[Provides Singleton]
    fun provideDatabase(@ApplicationContext context: Context): MyDB =
        Room.databaseBuilder(context, MyDB::class.java, "LocationDb")
            .allowMainThreadQueries()
            .build()

    @[Provides Singleton]
    fun provideLocationDao(appDB: MyDB): LocationDao =
        appDB.locationDao()

}