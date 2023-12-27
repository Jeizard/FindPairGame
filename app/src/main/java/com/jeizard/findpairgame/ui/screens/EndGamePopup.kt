package com.jeizard.findpairgame.ui.screens

import android.content.res.Configuration
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jeizard.findpairgame.COIN_COLOR
import com.jeizard.findpairgame.ELEMENTS_COLOR
import com.jeizard.findpairgame.R
import com.jeizard.findpairgame.strings.COINS_ICON_DESCRIPTION
import com.jeizard.findpairgame.strings.CONGRATULATION
import com.jeizard.findpairgame.strings.CUP_ICON_DESCRIPTION
import com.jeizard.findpairgame.strings.DOUBLE_REWARD_BUTTON
import com.jeizard.findpairgame.strings.HOME_ICON_DESCRIPTION
import com.jeizard.findpairgame.strings.MENU_ROUTE
import com.jeizard.findpairgame.strings.WIN_MESSAGE
import com.jeizard.findpairgame.viewmodels.CardsViewModel
import com.jeizard.findpairgame.viewmodels.CoinsViewModel
import com.jeizard.findpairgame.viewmodels.TimerViewModel
import kotlinx.coroutines.delay

@Composable
fun EndGamePopup(navController: NavController, coinViewModel: CoinsViewModel, timerViewModel: TimerViewModel, cardsViewModel: CardsViewModel) {
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.win_sound) }

    val configuration = LocalConfiguration.current
    val isWideScreen = if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
        false
    }
    else{
        configuration.screenHeightDp > 600
    }

    val allPairsFound by cardsViewModel.allPairsFound.observeAsState(false)
    LaunchedEffect(allPairsFound) {
        if (allPairsFound) {
            mediaPlayer.start()
            delay(1000)
            cardsViewModel.resetGame()
            coinViewModel.addCoins(coinViewModel.calculateReward(timerViewModel.getSeconds()))
        }
    }
    val reward = coinViewModel.calculateReward(timerViewModel.getSeconds())
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(isWideScreen) {
                Image(
                    painter = painterResource(id = R.drawable.ic_cup),
                    contentDescription = CUP_ICON_DESCRIPTION,
                    modifier = Modifier.size(200.dp),
                )
            }
            Text(
                text = CONGRATULATION,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = WIN_MESSAGE,
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Box(
                modifier = Modifier
                    .background(color = ELEMENTS_COLOR, shape = RoundedCornerShape(12.dp))
                    .padding(vertical = 16.dp, horizontal = 96.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_coin),
                        contentDescription = COINS_ICON_DESCRIPTION,
                        tint = COIN_COLOR,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(36.dp)
                    )
                    Text(
                        text = "$reward",
                        fontSize = 24.sp,
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 36.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        coinViewModel.addCoins(reward)
                        navController.navigate(MENU_ROUTE)
                    },
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .padding(end = 8.dp)
                ) {
                    Text(text = DOUBLE_REWARD_BUTTON)
                }
                Button(
                    onClick = {
                        navController.navigate(MENU_ROUTE)
                    },
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(ELEMENTS_COLOR),
                    modifier = Modifier
                        .height(60.dp)
                        .padding(start = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_home),
                        contentDescription = HOME_ICON_DESCRIPTION,
                        tint = Color.Black,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }
}