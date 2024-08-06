package uz.gita.demo_my_taxi.presenter.screen.map.component

import CustomButton
import android.annotation.SuppressLint
import android.content.res.Resources.Theme
import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mytaxitask.util.restoreMapView
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.gita.dem_my_taxi.R
import uz.gita.demo_my_taxi.presenter.screen.map.MapScreenContract
import uz.gita.demo_my_taxi.ui.theme.black
import uz.gita.demo_my_taxi.ui.theme.errorColor
import uz.gita.demo_my_taxi.ui.theme.greenColor
import uz.gita.demo_my_taxi.util.component.AppTextView
import uz.gita.demo_my_taxi.util.component.MyIconButton

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreenMainContent(
    mapView: MapView,
    busyOrActive: Boolean,
    zoom: Double,
    lastLatLng: LatLng = LatLng(0.0, 0.0),
    fullBootSheet: Boolean,
    scaffoldState: BottomSheetScaffoldState,
    scope: CoroutineScope,
    onEventDispatcher: (MapScreenContract.Intent) -> Unit
) {
    val context = LocalContext.current
    val progressBarColor = MaterialTheme.colorScheme.primary
    var isLoading by remember { mutableStateOf(false) }
    val mediaPlayer by remember { mutableStateOf(MediaPlayer.create(context, R.raw.click_sound)) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize()) {
            AndroidView(factory = { mapView })

            Row(modifier = Modifier.fillMaxWidth()) {
                MyIconButton(
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp, end = 12.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { }
                        .background(MaterialTheme.colorScheme.background)
                        .size(56.dp),
                    icon = R.drawable.ic_menu,
                    iconSize = 24,
                    childBoxColor = MaterialTheme.colorScheme.primaryContainer,
                    iconColor = MaterialTheme.colorScheme.onBackground
                )

                Row(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .height(56.dp)
                        .weight(1f)
                        .background(
                            MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(14.dp)
                        )
                ) {
                    CustomButton(
                        text = stringResource(id = R.string.busy),
                        onClick = {
                            onEventDispatcher(
                                MapScreenContract.Intent.ClickBusyOrActive(false)
                            )
                            isLoading = true
                            scope.launch {
                                delay(500)
                                isLoading = false
                            }
                        },
                        enable = !busyOrActive,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        enableBackgroundColor = errorColor,
                        textFontSize = 18,
                        backgroundColor = MaterialTheme.colorScheme.background,
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxHeight()
                            .weight(1f)
                    )
                    CustomButton(
                        text = stringResource(id = R.string.active),
                        textFontSize = 18,
                        onClick = {
                            mediaPlayer.start()
                            onEventDispatcher(
                                MapScreenContract.Intent.ClickBusyOrActive(true)
                            )
                            isLoading = true
                            scope.launch {
                                delay(500)
                                isLoading = false
                            }
                        },
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        enableBackgroundColor = greenColor,
                        enable = busyOrActive,
                        backgroundColor = MaterialTheme.colorScheme.background,
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxHeight()
                            .weight(1f)
                    )

                    AnimatedVisibility(
                        visible = isLoading,
                        enter = fadeIn(animationSpec = tween(durationMillis = 20)),
                        exit = fadeOut(animationSpec = tween(durationMillis = 20))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier

                                    .height(24.dp)
                                    .width(24.dp),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(top = 16.dp, start = 12.dp, end = 16.dp)
                        .background(
                            MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .size(56.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxSize()
                            .background(
                                color = greenColor,
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        AppTextView(
                            text = "95",
                            color = black,
                            fontSize = 20,
                            fontWeight = 700,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }

            MapScreenColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center),
                clickButtonScaleNear = {
                    if (zoom < 27)
                        onEventDispatcher(MapScreenContract.Intent.ClickButtonScaleNear(zoom + 1))
                },
                clickButtonScaleFar = {
                    if (zoom >= 1)
                        onEventDispatcher(MapScreenContract.Intent.ClickButtonScaleFar(zoom - 1))
                },
                clickButtonNavigation = {
                    mapView.getMapAsync { mapBoxMap ->
                        onEventDispatcher(MapScreenContract.Intent.ClickButtonScaleNear(15.0))
                        restoreMapView(mapBoxMap, lastLatLng)
                    }
                },
                clickButtonChevronUp = {
                    scope.launch {
                        scaffoldState.bottomSheetState.expand()
                    }
                    onEventDispatcher(MapScreenContract.Intent.ClickButtonChevronUp(true))
                },
                visibility = !fullBootSheet
            )
        }
    }
}
