package uz.gita.demo_my_taxi.service.impl

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import uz.gita.demo_my_taxi.service.LocationService
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import uz.gita.demo_my_taxi.MainActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationServiceImpl @Inject constructor(
    private val context: Context
) : Service(), LocationService {

    private lateinit var locationManager: LocationManager
    private val CHANNEL_ID = "LocationServiceChannel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        requestLocationUpdates()
    }

    init {
        locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun requestLocationUpdates() {
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // Location changes handled in getLatLng()
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        }

        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permissions if not granted
            return
        }

        // Request location updates using GPS_PROVIDER for better accuracy and more frequent updates
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            5000L, // 5 seconds
            0.1f,
            locationListener
        )
    }

    override fun getLatLng(): Flow<LatLng> = callbackFlow {
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val latLng = LatLng(location.latitude, location.longitude)
                trySend(latLng).isSuccess
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        }

        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return@callbackFlow
        }

        // Request location updates using GPS_PROVIDER for better accuracy and more frequent updates
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            5000L, // 5 seconds
            0.1f,
            locationListener
        )

        awaitClose {
            locationManager.removeUpdates(locationListener)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Location Service Channel"
            val descriptionText = "Channel for Location Service"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Location Service")
            .setContentText("Running...")
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(1, getNotification())
        return START_STICKY
    }
}
