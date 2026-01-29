package com.example.movielist.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object AuthUiUtils {
    @Composable
    fun horizontalPadding(): Dp {
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp
        return when {
            screenWidth < 360.dp -> 16.dp
            screenWidth > 600.dp -> 48.dp
            else -> 24.dp
        }
    }

    @Composable
    fun responsiveDp(fraction: Float): Dp {
        val screenHeight = LocalConfiguration.current.screenHeightDp.dp
        val base = screenHeight * fraction
        return when {
            screenHeight < 600.dp -> base * 0.8f
            screenHeight > 1000.dp -> base * 1.2f
            else -> base
        }
    }

    @Composable
    fun responsiveSp(fraction: Float): TextUnit {
        val screenHeight = LocalConfiguration.current.screenHeightDp
        val baseSize = (screenHeight * fraction).sp
        return when {
            screenHeight < 600 -> baseSize * 0.9f
            screenHeight > 1000 -> baseSize * 1.1f
            else -> baseSize
        }
    }
}