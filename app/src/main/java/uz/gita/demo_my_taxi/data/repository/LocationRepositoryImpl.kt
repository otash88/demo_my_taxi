package uz.gita.demo_my_taxi.data.repository

import uz.gita.demo_my_taxi.service.LocationService
import uz.gita.demo_my_taxi.domain.LocationRepository

import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.gita.demo_my_taxi.domain.AppRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val locationService: LocationService,

) : LocationRepository {

    override val locationFlow: Flow<LatLng> = flow{
        locationService.getLatLng().collect {

            emit(it)
        }

    }
}
