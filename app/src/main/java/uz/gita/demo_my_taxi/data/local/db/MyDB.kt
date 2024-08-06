package uz.gita.demo_my_taxi.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.gita.demo_my_taxi.data.local.dao.LocationDao
import uz.gita.demo_my_taxi.data.local.entity.LocationEntity

@Database(entities = [LocationEntity::class], version = 2, exportSchema = false)
abstract class MyDB : RoomDatabase() {
    abstract fun locationDao() : LocationDao

}