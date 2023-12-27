package com.jeizard.findpairgame.ui.screens

import android.content.res.Configuration
import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jeizard.findpairgame.ELEMENTS_COLOR
import com.jeizard.findpairgame.R
import com.jeizard.findpairgame.entities.Card
import com.jeizard.findpairgame.strings.CARD_IMAGE_DESCRIPTION
import com.jeizard.findpairgame.strings.END_GAME_ROUTE
import com.jeizard.findpairgame.ui.components.CoinsDisplay
import com.jeizard.findpairgame.ui.components.TimerDisplay
import com.jeizard.findpairgame.viewmodels.CardsViewModel
import com.jeizard.findpairgame.viewmodels.CoinsViewModel
import com.jeizard.findpairgame.viewmodels.TimerViewModel
import kotlinx.coroutines.delay

@Composable
fun GameScene(navController: NavController, coinViewModel: CoinsViewModel, timerViewModel: TimerViewModel, cardsViewModel: CardsViewModel) {
    val allPairsFound by cardsViewModel.allPairsFound.observeAsState(false)
    LaunchedEffect(allPairsFound) {
        if (allPairsFound) {
            timerViewModel.stopTimer()
            delay(1000)
            navController.navigate(END_GAME_ROUTE)
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimerDisplay(timerViewModel = timerViewModel)
                CoinsDisplay(coinViewModel = coinViewModel)
            }
            Spacer(modifier = Modifier.height(1.dp))
            Box(
                modifier = Modifier.weight(1f)
            ) {
                CardsGrid(cards = cardsViewModel.getCards(), cardsViewModel = cardsViewModel, timerViewModel = timerViewModel)
            }
        }
    }
}

@Composable
fun CardsGrid(cards: List<Card>, cardsViewModel: CardsViewModel, timerViewModel: TimerViewModel) {
    val configuration = LocalConfiguration.current
    val cardSize = if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
        val cardHeight = (configuration.screenHeightDp.dp - 120.dp) / 5
        val cardWidth = configuration.screenWidthDp.dp / 6
        minOf(cardHeight, cardWidth)
    }
    else{
        val cardHeight = (configuration.screenHeightDp.dp - 120.dp) / 6
        val cardWidth = configuration.screenWidthDp.dp / 5
        minOf(cardHeight, cardWidth)
    }
    val paddingStartAndEnd = if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
        (configuration.screenWidthDp.dp - cardSize * 5 - cardSize / 20 * 5 * 2) / 2
    }
    else{
        (configuration.screenWidthDp.dp - cardSize * 4 - cardSize / 20 * 4 * 2) / 2
    }
    val paddingTopAndBottom = if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
        ((configuration.screenHeightDp.dp - 120.dp) - cardSize * 4 - cardSize / 20 * 4 * 2) / 2
    }
    else{
        ((configuration.screenHeightDp.dp - 120.dp) - cardSize * 5 - cardSize / 20 * 5 * 2) / 2
    }
    Box(
        modifier = Modifier
            .padding(
                start = paddingStartAndEnd,
                end = paddingStartAndEnd,
                top = paddingTopAndBottom,
                bottom = paddingTopAndBottom
            )
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LazyHorizontalGrid(
                rows = GridCells.Fixed(4),
                reverseLayout = true
            ) {
                itemsIndexed(cards) { index, card ->
                    CardItem(
                        card = card,
                        cardIndex = index,
                        cardsViewModel = cardsViewModel,
                        timerViewModel = timerViewModel,
                        cardSize = cardSize
                    )
                }
            }
        }
        else{
            LazyVerticalGrid(
                columns = GridCells.Fixed(4)
            ) {
                itemsIndexed(cards) { index, card ->
                    CardItem(
                        card = card,
                        cardIndex = index,
                        cardsViewModel = cardsViewModel,
                        timerViewModel = timerViewModel,
                        cardSize = cardSize
                    )
                }
            }
        }
    }
}

@Composable
fun CardItem(card: Card, cardIndex: Int, cardsViewModel: CardsViewModel, timerViewModel: TimerViewModel, cardSize: Dp) {
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.flip_sound) }

    val configuration = LocalConfiguration.current

    LaunchedEffect(card.isFlipped.value) {
        if(configuration.orientation == cardsViewModel.getOrientation()){
            mediaPlayer.start()
            cardsViewModel.setCardsCount(0)
        }
        else if(cardsViewModel.getCards().size == cardsViewModel.getCardsCount() + 2){
            cardsViewModel.setOrientation(configuration.orientation)
        }
        else{
            cardsViewModel.setCardsCount(cardsViewModel.getCardsCount() + 1)
        }
    }

    Box(
        modifier = Modifier
            .padding(cardSize / 20)
            .size(cardSize)
            .aspectRatio(1f)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        cardsViewModel.selectCard(index = cardIndex)
                        timerViewModel.startTimer()
                    }
                )
            }
            .clip(RoundedCornerShape(cardSize / 5)),
        contentAlignment = Alignment.Center

    ) {
        AnimatedVisibility(
            visible = card.isFlipped.value,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = ELEMENTS_COLOR)
                    .clip(RoundedCornerShape(cardSize / 5))
            ) {
                Image(
                    painter = painterResource(id = card.image),
                    contentDescription = CARD_IMAGE_DESCRIPTION,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        AnimatedVisibility(
            visible = !card.isFlipped.value,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = ELEMENTS_COLOR)
                    .clip(RoundedCornerShape(cardSize / 5))
            ) {}
        }
    }
}