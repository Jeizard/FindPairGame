package com.jeizard.findpairgame.viewmodels

import android.content.res.Configuration
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeizard.findpairgame.R
import com.jeizard.findpairgame.entities.Card
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CardsViewModel : ViewModel() {

    private var cardImages = listOf(
        R.drawable.card_1, R.drawable.card_2,
        R.drawable.card_3, R.drawable.card_4,
        R.drawable.card_5, R.drawable.card_6,
        R.drawable.card_7, R.drawable.card_8,
        R.drawable.card_9, R.drawable.card_10)

    private var cards = mutableStateListOf<Card>()
    private var foundPairs = 0
    var allPairsFound = MutableLiveData(false)

    private var orientation = Configuration.ORIENTATION_LANDSCAPE
    private var cardsCount = 0

    init{
        generateCards()
    }

    private var firstCardIndex = -1
    private var secondCardIndex = -1

    private fun generateCards() {
        cards.clear()
        cards.addAll((cardImages + cardImages).shuffled().map { Card(it) })
    }

    fun getCards(): List<Card> {
        return cards
    }

    fun getOrientation(): Int{
        return orientation
    }

    fun setOrientation(orientation: Int){
        this.orientation = orientation
    }

    fun getCardsCount(): Int{
        return cardsCount
    }

    fun setCardsCount(cardsCount: Int){
        this.cardsCount = cardsCount
    }

    fun selectCard(index: Int){
        if(firstCardIndex == -1 && !cards[index].isFlipped.value){
            firstCardIndex = index
            cards[firstCardIndex].isFlipped.value = true
        }
        else if(secondCardIndex == -1 && firstCardIndex != index && !cards[index].isFlipped.value){
            secondCardIndex = index
            cards[secondCardIndex].isFlipped.value  = true
            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    if (cards[firstCardIndex].image != cards[secondCardIndex].image) {
                        delay(500)
                        cards[firstCardIndex].isFlipped.value = false
                        cards[secondCardIndex].isFlipped.value = false
                    }
                    else{
                        foundPairs++
                        allPairsFound.value = foundPairs == cards.size / 2
                    }
                    firstCardIndex = -1
                    secondCardIndex = -1
                }
            }
        }
    }

    fun resetGame() {
        foundPairs = 0
        allPairsFound.value = false
        firstCardIndex = -1
        secondCardIndex = -1
        generateCards()
    }
}
