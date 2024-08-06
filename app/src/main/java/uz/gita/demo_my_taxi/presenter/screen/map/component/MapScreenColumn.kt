package uz.gita.demo_my_taxi.presenter.screen.map.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import uz.gita.demo_my_taxi.ui.theme.navigationIconColor
import uz.gita.dem_my_taxi.R
import uz.gita.demo_my_taxi.util.component.MyIconButton

@Composable
fun MapScreenColumn(
    modifier: Modifier,
    visibility: Boolean,
    clickButtonScaleNear: () -> Unit,
    clickButtonScaleFar: () -> Unit,
    clickButtonNavigation: () -> Unit,
    clickButtonChevronUp: () -> Unit
) {
    Row(modifier = modifier) {

        AnimatedVisibility(
            modifier = Modifier,
            visible = visibility,
            enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut(),

            ) {

            MyIconButton(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { clickButtonChevronUp() }
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
                    .size(56.dp),
                icon = R.drawable.ic_chevrons_up,
                iconSize = 24,
                childBox = true
            )

        }
        Spacer(modifier = Modifier.weight(1f))

        AnimatedVisibility(
            modifier = Modifier,
            visible = visibility,
            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut(),
        ) {

            Column {
                MyIconButton(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { clickButtonScaleNear() }
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
                        .size(56.dp),
                    icon = R.drawable.ic_plus,
                    iconSize = 24,
                )

                MyIconButton(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 16.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { clickButtonScaleFar() }
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
                        .size(56.dp),
                    icon = R.drawable.ic_remove,
                    iconSize = 26,
                )
                MyIconButton(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { clickButtonNavigation() }
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
                        .size(56.dp),
                    icon = R.drawable.ic_navigation,
                    iconSize = 24,
                    iconColor = navigationIconColor
                )
            }

        }

    }

}