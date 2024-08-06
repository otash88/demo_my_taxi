package uz.gita.demo_my_taxi.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "LocationEntity")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var lat: Double,
    var lon: Double,
)
