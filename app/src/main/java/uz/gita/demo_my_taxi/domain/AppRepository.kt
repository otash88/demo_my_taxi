package uz.gita.demo_my_taxi.domain

interface AppRepository {
    fun addLatLong(lat: Double, long: Double)
}