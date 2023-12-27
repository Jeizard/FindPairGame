package com.jeizard.findpairgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jeizard.findpairgame.strings.END_GAME_ROUTE
import com.jeizard.findpairgame.strings.GAME_ROUTE
import com.jeizard.findpairgame.strings.MENU_ROUTE
import com.jeizard.findpairgame.ui.screens.EndGamePopup
import com.jeizard.findpairgame.ui.screens.GameScene
import com.jeizard.findpairgame.ui.screens.MenuView
import com.jeizard.findpairgame.ui.theme.FindPairGameTheme
import com.jeizard.findpairgame.viewmodels.CardsViewModel
import com.jeizard.findpairgame.viewmodels.CoinsViewModel
import com.jeizard.findpairgame.viewmodels.TimerViewModel
import com.jeizard.findpairgame.viewmodels.factories.CoinsViewModelFactory

val ELEMENTS_COLOR = Color.LightGray
val COIN_COLOR = Color(0xFFFFD500)

class MainActivity : ComponentActivity() {
    private lateinit var coinViewModel: CoinsViewModel
    private val timerViewModel: TimerViewModel by viewModels()
    private val cardsViewModel: CardsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        coinViewModel = ViewModelProvider(this, CoinsViewModelFactory(this))[CoinsViewModel::class.java]
        setContent {
            FindPairGameTheme {
                val navController = rememberNavController()
                setupBackPressHandler(navController)

                NavHost(navController = navController, startDestination = MENU_ROUTE) {
                    setupNavigationGraph(navController)
                }
            }
        }
    }

    private fun setupBackPressHandler(navController: NavController) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.currentBackStackEntry?.destination?.route != MENU_ROUTE) {
                    navController.navigate(MENU_ROUTE) {
                        popUpTo(MENU_ROUTE) {
                            inclusive = true
                        }
                    }
                } else {
                    finish()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this@MainActivity, callback)
    }

    private fun NavGraphBuilder.setupNavigationGraph(navController: NavController) {
        composable(MENU_ROUTE) {
            MenuView(navController, coinViewModel, timerViewModel, cardsViewModel)
        }
        composable(GAME_ROUTE) {
            GameScene(navController, coinViewModel, timerViewModel, cardsViewModel)
        }
        composable(END_GAME_ROUTE) {
            EndGamePopup(navController, coinViewModel, timerViewModel, cardsViewModel)
        }
    }
}



