package uz.gita.demo_my_taxi.presenter.screen.map.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.gita.dem_my_taxi.R
import uz.gita.dem_my_taxi.R.drawable
import uz.gita.demo_my_taxi.util.component.RowBottomSheetComponent

@Composable
fun MapScreenBottomSheetContent() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary)
        ) {
            RowBottomSheetComponent(
                icon = drawable.ic_tariff,
                title = stringResource(id = R.string.tariff),
                text = "6/8",
                clickable = {})
            HorizontalDivider(
                thickness = 0.7.dp,
                color = MaterialTheme.colorScheme.tertiary
            )
           RowBottomSheetComponent(
                icon = drawable.ic_order,
                title = stringResource(id = R.string.orders),
                text = "0",
                clickable = {})
            HorizontalDivider(
                thickness = 0.7.dp,
                color = MaterialTheme.colorScheme.tertiary
            )
            RowBottomSheetComponent(
                icon = drawable.ic_rocket,
                title = stringResource(id = R.string.thereIs),
                clickable = {})

        }
    }
}