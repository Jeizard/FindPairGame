package com.jeizard.findpairgame.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jeizard.findpairgame.R
import com.jeizard.findpairgame.strings.GAME_ROUTE
import com.jeizard.findpairgame.strings.LOGO_ICON_DESCRIPTION
import com.jeizard.findpairgame.strings.PLAY_BUTTON
import com.jeizard.findpairgame.strings.PRIVACY_ICON_DESCRIPTION
import com.jeizard.findpairgame.ui.components.CoinsDisplay
import com.jeizard.findpairgame.ui.components.PrivacyButton
import com.jeizard.findpairgame.viewmodels.CardsViewModel
import com.jeizard.findpairgame.viewmodels.CoinsViewModel
import com.jeizard.findpairgame.viewmodels.TimerViewModel

@Composable
fun MenuView(navController: NavController, coinViewModel: CoinsViewModel, timerViewModel: TimerViewModel, cardsViewModel: CardsViewModel) {
    val configuration = LocalConfiguration.current
    val isWideScreen = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        false
    } else {
        configuration.screenHeightDp > 600
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        CoinsDisplay(coinViewModel = coinViewModel)
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isWideScreen) {
                Image(
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = LOGO_ICON_DESCRIPTION,
                    modifier = Modifier.size(280.dp)
                        .padding(bottom = 32.dp),
                    alignment = Alignment.Center

                )
            }
            Box(
                modifier = Modifier.padding(
                    bottom = 16.dp,
                    top = 16.dp,
                    end = 100.dp,
                    start = 100.dp
                ),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(
                    onClick = {
                        timerViewModel.stopTimer()
                        timerViewModel.resetTimer()
                        cardsViewModel.resetGame()
                        navController.navigate(GAME_ROUTE)
                    },
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Text(text = PLAY_BUTTON)
                }
            }
        }
        PrivacyButton()
    }
}