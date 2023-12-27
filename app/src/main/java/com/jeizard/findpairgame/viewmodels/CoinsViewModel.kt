package com.jeizard.findpairgame.viewmodels

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel

private const val COINS_KEY = "coins"
private const val COIN_PREFERENCES_KEY = "coin_preferences"

class CoinsViewModel(private val context: Context) : ViewModel()  {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        COIN_PREFERENCES_KEY,
        Context.MODE_PRIVATE
    )

    private val coinCount = mutableIntStateOf(sharedPreferences.getInt(COINS_KEY, 100))

    fun getCoins(): Int {
        return coinCount.value
    }

    fun addCoins(amount: Int) {
        coinCount.value += amount
        saveCoins()
    }

    fun removeCoins(amount: Int): Boolean {
        if (coinCount.value >= amount) {
            coinCount.value -= amount
            saveCoins()
            return true
        }
        return false
    }

    private fun saveCoins() {
        sharedPreferences.edit().putInt(COINS_KEY, coinCount.value).commit()
    }

    fun calculateReward(seconds: Int): Int {
        val maxReward = 100
        val minReward = 10
        val additionalRewardPerSecond = 5

        if (seconds <= 20) {
            return maxReward
        }

        return maxOf(minReward, maxReward - (seconds - 20) * additionalRewardPerSecond)
    }
}