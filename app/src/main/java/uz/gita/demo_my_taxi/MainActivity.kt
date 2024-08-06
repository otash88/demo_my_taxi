package uz.gita.demo_my_taxi

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import cafe.adriel.voyager.navigator.Navigator
import com.mapbox.mapboxsdk.Mapbox
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.demo_my_taxi.domain.AppRepository
import uz.gita.demo_my_taxi.presenter.screen.map.MapScreen
import uz.gita.demo_my_taxi.service.impl.LocationServiceImpl
import uz.gita.demo_my_taxi.ui.theme.Demo_my_taxiTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var repository: AppRepository

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startLocationService()
            setMapContent()
        } else {
            // showPermissionDeniedDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this)

        if (checkLocationPermission()) {
            startLocationService()
            setMapContent()
        } else {
            requestLocationPermission()
        }
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun startLocationService() {
        val serviceIntent = Intent(this, LocationServiceImpl::class.java)
        startService(serviceIntent)
    }

    private fun setMapContent() {
        setContent {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Demo_my_taxiTheme {
                    Navigator(MapScreen())
                }
            }
        }
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Location Permission Denied")
            setMessage("This app requires location permissions to function properly.")
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }
}



