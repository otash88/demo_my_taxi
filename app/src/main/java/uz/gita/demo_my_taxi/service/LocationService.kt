package uz.gita.demo_my_taxi.service

import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.flow.Flow

interface LocationService {
    fun getLatLng(): Flow<LatLng>
}