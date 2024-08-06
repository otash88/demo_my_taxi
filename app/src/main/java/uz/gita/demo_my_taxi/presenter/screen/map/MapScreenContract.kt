package uz.gita.demo_my_taxi.presenter.screen.map

import com.mapbox.mapboxsdk.geometry.LatLng
import org.orbitmvi.orbit.ContainerHost

interface MapScreenContract {

    sealed interface MapScreenModel : ContainerHost<UiState, SideEffect> {

        fun onEventDispatcher(intent: Intent)
    }

    data class UiState (
        val fullBootSheet: Boolean = false,
        val busyOrActive: Boolean = false,
        val scale: Double = 15.0,
        val latLng: LatLng? = null,

    )

    sealed interface Intent {
        data class ClickBusyOrActive(
            val busyOrActive: Boolean
        ) : Intent

        data class SetLatLong(
            val latLng: LatLng
        ) : Intent

        data class ShowToast(
            val message: String
        ) : Intent

        data class ClickButtonScaleNear(
            val scale: Double
        ) : Intent

        data class ClickButtonScaleFar(
            val scale: Double
        ) : Intent

        data class ClickButtonChevronUp(
            val fullBootSheet: Boolean
        ) : Intent


    }

    sealed interface SideEffect {
        data class  ShowToast(val message: String) : SideEffect
    }
}