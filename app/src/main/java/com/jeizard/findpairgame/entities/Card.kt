package com.jeizard.findpairgame.entities

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class Card(val image: Int, var isFlipped: MutableState<Boolean> = mutableStateOf(false))