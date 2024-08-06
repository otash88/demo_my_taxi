package uz.gita.demo_my_taxi.data.repository


import uz.gita.demo_my_taxi.data.local.entity.LocationEntity
import uz.gita.demo_my_taxi.data.local.dao.LocationDao
import uz.gita.demo_my_taxi.domain.AppRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepositoryImpl @Inject constructor(
    private val dao: LocationDao
) : AppRepository {
    override fun addLatLong(lat: Double, long: Double) {
        dao.addLocation(LocationEntity(lat = lat, lon = long))
    }


}

