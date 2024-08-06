package uz.gita.demo_my_taxi.domain

import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    val locationFlow: Flow<LatLng>
}