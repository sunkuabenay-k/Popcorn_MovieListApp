package com.example.movielist.ui.util

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Responsive {

    @Composable
    private fun base(): Dp {
        val config = LocalConfiguration.current
        return if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            config.screenWidthDp.dp
        } else {
            config.screenHeightDp.dp
        }
    }

    @Composable
    fun screenWidth(): Dp =
        LocalConfiguration.current.screenWidthDp.dp

    @Composable
    fun screenHeight(): Dp =
        LocalConfiguration.current.screenHeightDp.dp

    @Composable
    fun dp(fraction: Float): Dp =
        base() * fraction

    @Composable
    fun sp(fraction: Float): TextUnit =
        (base().value * fraction).sp

    @Composable
    fun horizontalPadding(): Dp {
        val width = screenWidth()
        return when {
            width < 360.dp -> 16.dp
            width < 600.dp -> 24.dp
            else -> 48.dp
        }
    }

    @Composable
    fun dpFromWidth(fraction: Float): Dp =
        screenWidth() * fraction

}
