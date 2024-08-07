package uz.gita.demo_my_taxi.presenter.screen.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.mapboxsdk.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.container
import uz.gita.demo_my_taxi.domain.AppRepository
import uz.gita.demo_my_taxi.domain.LocationRepository
import javax.inject.Inject

@HiltViewModel
class MapScreenViewModel @Inject constructor(
    private val repository: AppRepository,
    private val locationRepositoryd: LocationRepository
) : ViewModel(), MapScreenContract.MapScreenModel {

    private val locationFlow: Flow<LatLng> = locationRepository.locationFlow

    init {
        viewModelScope.launch {
            locationFlow.collect { latLng ->
                repository.addLatLong(lat = latLng.latitude, long = latLng.longitude)
                onEventDispatcher(MapScreenContract.Intent.SetLatLong(latLng))
                onEventDispatcher(MapScreenContract.Intent.ShowToast("Location is: ${latLng.latitude} ${latLng.longitude}"))
            }
        }
    }

    override fun onEventDispatcher(intent: MapScreenContract.Intent) {
        when (intent) {


            is MapScreenContract.Intent.ClickButtonScaleFar -> {
                intent {
                    reduce {
                        state.copy(scale = intent.scale)
                    }
                }
            }

            is MapScreenContract.Intent.ClickButtonScaleNear -> {
                intent {
                    reduce {
                        state.copy(scale = intent.scale)
                    }
                }
            }

            is MapScreenContract.Intent.SetLatLong -> {
                intent {
                    reduce {
                        state.copy(latLng = intent.latLng)
                    }
                }

            }

            is MapScreenContract.Intent.ShowToast -> {
                intent {
                    postSideEffect(MapScreenContract.SideEffect.ShowToast(intent.message))
                }

            }

            is MapScreenContract.Intent.ClickBusyOrActive -> {
                intent {
                    reduce {
                        state.copy(busyOrActive = intent.busyOrActive)
                    }
                }
            }

            is MapScreenContract.Intent.ClickButtonChevronUp -> {
                intent {
                    reduce {
                        state.copy(fullBootSheet = intent.fullBootSheet)
                    }
                }
            }
        }
    }

    override val container =
        container<MapScreenContract.UiState, MapScreenContract.SideEffect>(MapScreenContract.UiState())

}