package com.jeizard.findpairgame

import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jeizard.findpairgame.entities.Card
import com.jeizard.findpairgame.ui.theme.FindPairGameTheme
import com.jeizard.findpairgame.viewmodels.CardsViewModel
import com.jeizard.findpairgame.viewmodels.CoinsViewModel
import com.jeizard.findpairgame.viewmodels.TimerViewModel
import com.jeizard.findpairgame.viewmodels.factories.CoinsViewModelFactory
import kotlinx.coroutines.delay

private const val COINS_ICON_DESCRIPTION = "Coins icon"
private const val TIMER_ICON_DESCRIPTION = "Timer icon"
private const val LOGO_ICON_DESCRIPTION = "Logo icon"
private const val CUP_ICON_DESCRIPTION = "Cup icon"
private const val PRIVACY_ICON_DESCRIPTION = "Settings Icon"
private const val HOME_ICON_DESCRIPTION ="Home Icon"
private const val  CARD_IMAGE_DESCRIPTION = "Card image"

private const val PLAY_BUTTON = "ИГРАТЬ"
private const val DOUBLE_REWARD_BUTTON = "Двойная награда"
private const val CONGRATULATION = "Поздравляем!"
private const val WIN_MESSAGE = "Вы выиграли!"

private const val MENU_ROUTE = "menu"
private const val GAME_ROUTE = "game"
private const val END_GAME_ROUTE = "victory"

private val ELEMENTS_COLOR = Color.LightGray
private val COIN_COLOR = Color(0xFFFFD500)

class MainActivity : ComponentActivity() {
    private lateinit var coinViewModel: CoinsViewModel
    private val timerViewModel: TimerViewModel by viewModels() // как работает?
    private val cardsViewModel: CardsViewModel by viewModels() // как работает?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        coinViewModel = ViewModelProvider(this, CoinsViewModelFactory(this))[CoinsViewModel::class.java]

        setContent {
            FindPairGameTheme {
                val navController = rememberNavController()

                LaunchedEffect(true) {
                    val callback = object : OnBackPressedCallback(true) {
                        override fun handleOnBackPressed() {
                            if (navController.currentBackStackEntry?.destination?.route != MENU_ROUTE) {
                                navController.navigate(MENU_ROUTE) {
                                    popUpTo(MENU_ROUTE) {
                                        inclusive = true // что делает?
                                    }
                                }
                            } else {
                                finish()
                            }
                        }
                    }
                    onBackPressedDispatcher.addCallback(this@MainActivity, callback)
                }


                NavHost(navController = navController, startDestination = MENU_ROUTE) {
                    composable(MENU_ROUTE) {
                        MenuView(
                            navController = navController,
                            coinViewModel = coinViewModel,
                            timerViewModel = timerViewModel,
                            cardsViewModel = cardsViewModel
                        )
                    }
                    composable(GAME_ROUTE) {
                        GameScene(
                            navController = navController,
                            coinViewModel = coinViewModel,
                            timerViewModel = timerViewModel,
                            cardsViewModel = cardsViewModel
                        )
                    }
                    composable(END_GAME_ROUTE) {
                        EndGamePopup(
                            navController = navController,
                            coinViewModel = coinViewModel,
                            timerViewModel = timerViewModel,
                            cardsViewModel = cardsViewModel
                        )
                    }
                }
            }
        }
    }
}

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

@Composable
fun TimerDisplay(timerViewModel: TimerViewModel) {
    var isBlinking = remember { mutableStateOf(false) }
    var isShaking = remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val scale = if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
        configuration.screenHeightDp /  380.0
    }
    else{
        configuration.screenHeightDp / 830.0
    }
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

@Composable
fun PrivacyButton() {
    val configuration = LocalConfiguration.current
    val iconSize = if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
        configuration.screenHeightDp / 5
    }
    else{
        configuration.screenWidthDp / 5
    }
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

@Composable
fun MenuView(navController: NavController, coinViewModel: CoinsViewModel, timerViewModel: TimerViewModel, cardsViewModel: CardsViewModel) {
    val configuration = LocalConfiguration.current
    val isWideScreen = if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
        false
    }
    else{
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
            if(isWideScreen) {
                Image(
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = LOGO_ICON_DESCRIPTION,
                    modifier = Modifier.size(280.dp)
                        .padding(bottom = 32.dp),
                    alignment = Alignment.Center

                )
            }
            Box(
                modifier = Modifier.padding(bottom = 16.dp, top = 16.dp, end = 100.dp, start = 100.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(
                    onClick = {
                        timerViewModel.stopTimer()
                        timerViewModel.resetTimer()
                        cardsViewModel.resetGame()
                        navController.navigate(GAME_ROUTE) },
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

@Composable
fun GameScene(navController: NavController, coinViewModel: CoinsViewModel, timerViewModel: TimerViewModel, cardsViewModel: CardsViewModel) {
    val configuration = LocalConfiguration.current

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



