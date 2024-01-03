package com.jeizard.findpairgame.ui.components

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jeizard.findpairgame.MetricsCalculator
import com.jeizard.findpairgame.R
import com.jeizard.findpairgame.strings.TIMER_ICON_DESCRIPTION
import com.jeizard.findpairgame.ui.theme.ELEMENTS_COLOR
import com.jeizard.findpairgame.viewmodels.TimerViewModel
import kotlinx.coroutines.delay

@Composable
fun TimerDisplay(timerViewModel: TimerViewModel) {
    val metricsCalculator = MetricsCalculator()
    val configuration = LocalConfiguration.current
    val scale = metricsCalculator.calculateDisplayScale(configuration)

    var isBlinking = remember { mutableStateOf(false) }
    var isShaking = remember { mutableStateOf(false) }

    fun toggleTimerBlink() {
        isBlinking.value = !isBlinking.value
    }

    fun toggleTimerShake() {
        isShaking.value = !isShaking.value
    }

    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.time_notify) }

    LaunchedEffect(timerViewModel.getSeconds()) {
        if (timerViewModel.getSeconds() == 20) {
            repeat(3) {
                toggleTimerBlink()
                delay(300)
            }
            isBlinking.value = false
        }
    }

    LaunchedEffect(timerViewModel.getSeconds()) {
        if(timerViewModel.getSeconds() == 19){
            delay(300)
            mediaPlayer.start()
        }
        if (timerViewModel.getSeconds() == 20) {
            repeat(9) {
                toggleTimerShake()
                delay(100)
            }
            isShaking.value = false
            mediaPlayer.stop()
        }
    }


    Box(
        modifier = Modifier.wrapContentSize(Alignment.TopStart)
            .scale(scale.toFloat())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(
                    top = 64.dp,
                    start = 32.dp
                )
                .background(
                    color = ELEMENTS_COLOR,
                    shape = RoundedCornerShape(24.dp)
                )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_timer),
                contentDescription = TIMER_ICON_DESCRIPTION,
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .graphicsLayer(2.2f, 2.2f)
                    .graphicsLayer(
                        rotationZ = if (isShaking.value) 10f else 0f
                    )
            )
            Text(
                text = timerViewModel.getTime(),
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 8.dp)
                    .graphicsLayer(
                        scaleX = if (isBlinking.value) 1.1f else 1f,
                        scaleY = if (isBlinking.value) 1.1f else 1f
                    ),
                color = if (timerViewModel.getSeconds() >= 20) Color.Red else Color.Black
            )
        }
    }
}