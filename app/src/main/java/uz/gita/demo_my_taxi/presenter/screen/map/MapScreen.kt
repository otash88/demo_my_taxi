package uz.gita.demo_my_taxi.presenter.screen.map

import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import com.example.mytaxitask.util.YOUR_MAPTILER_API_KEY
import com.example.mytaxitask.util.getDrawableToBitmap
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.maps.MapView
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import uz.gita.dem_my_taxi.R
import uz.gita.demo_my_taxi.presenter.screen.map.MapScreenContract.UiState
import uz.gita.demo_my_taxi.presenter.screen.map.component.MapScreenBottomSheetContent
import uz.gita.demo_my_taxi.presenter.screen.map.component.MapScreenMainContent

class MapScreen : Screen {


    @Composable
    override fun Content() {
        val model: MapScreenContract.MapScreenModel = getViewModel<MapScreenViewModel>()
        val uiState = model.collectAsState().value
        val context = LocalContext.current


        model.collectSideEffect(sideEffect = { sideEffect ->
            when (sideEffect) {
                is MapScreenContract.SideEffect.ShowToast -> {
                    Toast.makeText(context, sideEffect.message, Toast.LENGTH_LONG).show()
                }
            }
        })
        MapScreenContent(uiState = uiState, onEventDispatcher = model::onEventDispatcher)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreenContent(
    uiState: UiState,
    onEventDispatcher: (MapScreenContract.Intent) -> Unit
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    var zoom by remember { mutableDoubleStateOf(15.toDouble()) }
    val lifecycleOwner = LocalLifecycleOwner.current
    var fullBootSheet by remember { mutableStateOf(false) }
    val scaffoldState = rememberBottomSheetScaffoldState()
    val bottomSheetState by remember { mutableStateOf(scaffoldState.bottomSheetState) }

    var marker by remember { mutableStateOf<Marker?>(null) }
    var busyOrActive by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var lastLatLng by remember { mutableStateOf(uiState.latLng) }
    val isSystemInDarkMode = isSystemInDarkTheme()
    var isDarkMode by remember { mutableStateOf(isSystemInDarkMode) }


    LaunchedEffect(isSystemInDarkMode) {
        isDarkMode = isSystemInDarkMode
    }

    LaunchedEffect(bottomSheetState.currentValue) {
        onEventDispatcher(MapScreenContract.Intent.ClickButtonChevronUp(bottomSheetState.currentValue == SheetValue.Expanded))
    }

    LaunchedEffect(
        uiState.scale,
        uiState.busyOrActive,
        uiState.fullBootSheet,
        uiState.latLng
    ) {
        zoom = uiState.scale
        fullBootSheet = uiState.fullBootSheet
        busyOrActive = uiState.busyOrActive
        if (uiState.latLng != null) {
            lastLatLng = uiState.latLng
        }
    }

    DisposableEffect(isDarkMode) {
        mapView.getMapAsync { mapboxMap ->
            if (isDarkMode) {
                mapboxMap.setStyle("https://api.maptiler.com/maps/streets-v2-dark/style.json?key=$YOUR_MAPTILER_API_KEY")
            } else {
                mapboxMap.setStyle("https://api.maptiler.com/maps/streets/style.json?key=$YOUR_MAPTILER_API_KEY")
            }
        }
        onDispose {}
    }

    DisposableEffect(zoom) {
        mapView.getMapAsync { mapboxMap ->
            mapboxMap.cameraPosition.let { currentPosition ->
                val cameraPosition = CameraPosition.Builder()
                    .target(currentPosition.target)
                    .zoom(zoom)
                    .build()
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }
        onDispose {}
    }

    DisposableEffect(lastLatLng) {
        mapView.getMapAsync { mapboxMap ->
            mapboxMap.apply {
                if (marker == null) {
                    val bitmap = getDrawableToBitmap(
                        context = context,
                        drawableId = R.drawable.ic_car
                    )
                    val icon = bitmap.let { IconFactory.getInstance(context).fromBitmap(it) }
                    val cameraPosition = CameraPosition.Builder()
                        .target(lastLatLng)
                        .zoom(zoom)
                        .build()

                    animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

                    val markerOptions = MarkerOptions()
                        .position(lastLatLng)
                        .icon(icon)

                    marker = addMarker(markerOptions)
                } else {
                    marker?.let {
                        it.position = lastLatLng
                    }
                }
            }
        }
        onDispose {}
    }
    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }


        lifecycle.addObserver(lifecycleObserver)


        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
            mapView.onDestroy()
        }
    }
    BottomSheetScaffold(

        scaffoldState = scaffoldState,
        sheetContainerColor = MaterialTheme.colorScheme.background.copy(alpha = 0f),
        sheetPeekHeight = 170.dp,

        sheetShadowElevation = 0.dp,

        sheetContent = {
            MapScreenBottomSheetContent()
        },
        content = {
            lastLatLng?.let { latLng ->
                MapScreenMainContent(
                    scaffoldState = scaffoldState,
                    scope = scope,
                    onEventDispatcher = onEventDispatcher,
                    mapView = mapView,
                    busyOrActive = busyOrActive,
                    fullBootSheet = fullBootSheet,
                    lastLatLng = latLng,
                    zoom = zoom
                )
            }
        }
    )
}


