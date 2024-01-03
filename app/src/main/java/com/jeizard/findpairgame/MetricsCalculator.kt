package com.jeizard.findpairgame

import android.content.res.Configuration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private const val STANDARD_LANDSCAPE_SCREEN_HEIGHT = 380.0
private const val STANDARD_PORTRAIT_SCREEN_HEIGHT = 830.0
private const val MIN_SCREEN_HEIGHT_TO_ICON = 600.0

private const val STANDARD_HEIGHT_PADDING = 120

const val CARDS_NUMBER = 20
const val ROW_CARDS_NUMBER = 4
const val COLUMN_CARDS_NUMBER = 5

class MetricsCalculator {
    fun calculateDisplayScale(configuration: Configuration): Double{
        val scale = if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            configuration.screenHeightDp / STANDARD_LANDSCAPE_SCREEN_HEIGHT
        }
        else{
            configuration.screenHeightDp / STANDARD_PORTRAIT_SCREEN_HEIGHT
        }
        return scale
    }

    fun calculateIconSize(configuration: Configuration): Int{
        val iconSize = if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            configuration.screenHeightDp / 5
        }
        else{
            configuration.screenWidthDp / 5
        }
        return iconSize
    }

    fun shouldDisplayIcon(configuration: Configuration): Boolean {
        val isDisplayIcon = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            false
        } else {
            configuration.screenHeightDp > MIN_SCREEN_HEIGHT_TO_ICON
        }
        return isDisplayIcon
    }

    fun calculateCardSize(configuration: Configuration): Dp {
        val cardSize = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val cardHeight = (configuration.screenHeightDp.dp - STANDARD_HEIGHT_PADDING.dp) / (ROW_CARDS_NUMBER + 1)
            val cardWidth = configuration.screenWidthDp.dp / (COLUMN_CARDS_NUMBER + 1)
            minOf(cardHeight, cardWidth)
        } else {
            val cardHeight = (configuration.screenHeightDp.dp - STANDARD_HEIGHT_PADDING.dp) / (COLUMN_CARDS_NUMBER + 1)
            val cardWidth = configuration.screenWidthDp.dp / (ROW_CARDS_NUMBER + 1)
            minOf(cardHeight, cardWidth)
        }
        return cardSize
    }


    fun calculateStartAndEndPadding(configuration: Configuration, cardSize: Dp): Dp {
        val paddingStartAndEnd =
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                (configuration.screenWidthDp.dp - cardSize * COLUMN_CARDS_NUMBER - cardSize / CARDS_NUMBER * COLUMN_CARDS_NUMBER * 2) / 2
            } else {
                (configuration.screenWidthDp.dp - cardSize * ROW_CARDS_NUMBER - cardSize / CARDS_NUMBER * ROW_CARDS_NUMBER * 2) / 2
            }
        return paddingStartAndEnd
    }

    fun calculateTopAndBottomPadding(configuration: Configuration, cardSize: Dp): Dp {
        val paddingTopAndBottom =
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                ((configuration.screenHeightDp.dp - STANDARD_HEIGHT_PADDING.dp) - cardSize * ROW_CARDS_NUMBER - cardSize / CARDS_NUMBER * ROW_CARDS_NUMBER * 2) / 2
            } else {
                ((configuration.screenHeightDp.dp - STANDARD_HEIGHT_PADDING.dp) - cardSize * COLUMN_CARDS_NUMBER - cardSize / CARDS_NUMBER * COLUMN_CARDS_NUMBER * 2) / 2
            }
        return paddingTopAndBottom
    }
}