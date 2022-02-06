package com.jonandpaul.jonandpaul.ui.theme

import androidx.compose.material.lightColors
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun JonAndPaulTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        content = content,
        colorScheme = colorScheme,
        typography = JonAndPaulTypography
    )
}

val colorScheme = ColorScheme(
    background = Color.White,
    onBackground = Black900,
    surface = Color.White,
    inverseOnSurface = Black900,
    inverseSurface = Black900,
    onSurfaceVariant = Black900,
    onSurface = Black900,
    surfaceVariant = Black900,
    primary = Black900,
    onPrimary = Color.White,
    primaryContainer = Color.White,
    onPrimaryContainer = Black900,
    inversePrimary = Color.White,
    error = Red900,
    errorContainer = Color.White,
    onError = Black900,
    onErrorContainer = Black900,
    secondary = Black900,
    secondaryContainer = Color.White,
    onSecondary = Color.White,
    onSecondaryContainer = Black900,
    tertiary = Black800,
    onTertiaryContainer = Black800,
    onTertiary = Color.White,
    tertiaryContainer = Color.White,
    outline = Black800,
)

private val lightColors = lightColors(
    primary = Black900,
    primaryVariant = Black900,
    onPrimary = Color.White,
    secondary = Black900,
    secondaryVariant = Black900,
    onSecondary = Color.White,
    error = Red900,
    background = Color.White,
    onBackground = Black900,
    onSurface = Black900
)