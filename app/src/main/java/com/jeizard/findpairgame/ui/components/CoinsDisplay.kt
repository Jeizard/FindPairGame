package com.jeizard.findpairgame.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jeizard.findpairgame.COIN_COLOR
import com.jeizard.findpairgame.R
import com.jeizard.findpairgame.strings.COINS_ICON_DESCRIPTION
import com.jeizard.findpairgame.viewmodels.CoinsViewModel

@Composable
fun CoinsDisplay(coinViewModel: CoinsViewModel) {
    val configuration = LocalConfiguration.current
    val scale = if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
        configuration.screenHeightDp / 380.0
    }
    else{
        configuration.screenHeightDp / 830.0
    }

    Box(
        modifier = Modifier.wrapContentSize(Alignment.TopEnd)
            .scale(scale.toFloat())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 64.dp,
                end = 32.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_coin),
                contentDescription = COINS_ICON_DESCRIPTION,
                tint = COIN_COLOR,
                modifier = Modifier
                    .padding(end = 24.dp)
                    .scale(1.8f)
            )
            Text(
                text = "${coinViewModel.getCoins()}",
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .scale(1.5f)
            )
        }
    }
}