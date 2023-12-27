package com.jeizard.findpairgame.viewmodels

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class TimerViewModel : ViewModel() {
    private var job: Job? = null
    private var seconds = mutableIntStateOf(0)
    private var isTimerRunning: Boolean = false

    private val scope = CoroutineScope(Dispatchers.Default)

    fun startTimer() {
        if (!isTimerRunning) {
            job = scope.launch {
                while (true) {
                    delay(1000)
                    seconds.value++
                }
            }
            isTimerRunning = true
        }
    }

    fun stopTimer() {
        job?.cancel()
        isTimerRunning = false
    }

    fun resetTimer(){
        seconds.value = 0
    }

    fun getSeconds(): Int {
        return seconds.value
    }

    fun getTime(): String {
        val minutes = seconds.value / 60
        val seconds = seconds.value % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
