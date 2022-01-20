package com.jonandpaul.jonandpaul.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun JonAndPaulTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        shapes = Shapes,
        content = content,
        typography = JonAndPaulTypography,
        colors = lightColors
    )
}

private val lightColors = lightColors(
    primary = Color.White,
    primaryVariant = Color.White,
    onPrimary = Black900,
    secondary = Black900,
    secondaryVariant = Black900,
    onSecondary = Color.White,
    error = Red900,
    background = Color.White,
    onBackground = Black900
)