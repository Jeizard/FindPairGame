package com.jeizard.findpairgame.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jeizard.findpairgame.MetricsCalculator
import com.jeizard.findpairgame.R
import com.jeizard.findpairgame.strings.PRIVACY_ICON_DESCRIPTION
import com.jeizard.findpairgame.ui.theme.ELEMENTS_COLOR

@Composable
fun PrivacyButton() {
    val metricsCalculator = MetricsCalculator()
    val configuration = LocalConfiguration.current
    val iconSize = metricsCalculator.calculateIconSize(configuration)

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.BottomEnd)
            .padding(end = 32.dp, bottom = 32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(ELEMENTS_COLOR)
            .clickable {

            },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_privacy),
            contentDescription = PRIVACY_ICON_DESCRIPTION,
            tint = Color.Black,
            modifier = Modifier
                .padding(8.dp)
                .size(iconSize.dp)
        )
    }
}