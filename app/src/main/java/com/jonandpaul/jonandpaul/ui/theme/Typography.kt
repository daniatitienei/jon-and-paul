package com.jonandpaul.jonandpaul.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jonandpaul.jonandpaul.R

val Lato = FontFamily(
    Font(R.font.lato_regular),
    Font(R.font.lato_light, weight = FontWeight.Light),
    Font(R.font.lato_bold, weight = FontWeight.Bold),
    Font(R.font.lato_black, weight = FontWeight.ExtraBold)
)

val JonAndPaulTypography = Typography(
    titleLarge = TextStyle(
        fontWeight = FontWeight.W600,
        fontSize = 20.sp,
        color = Black900
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.W600,
        fontSize = 16.sp,
        color = Black900
    ),
    titleSmall = TextStyle(
        fontWeight = FontWeight.W600,
        fontSize = 12.sp,
        color = Black900
    ),
    bodySmall = TextStyle(
        fontSize = 12.sp,
        color = Black900,
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        color = Black900,
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        color = Black900,
    ),
    labelSmall = TextStyle(
        fontSize = 12.sp,
        color = Black900,
    ),
    labelMedium = TextStyle(
        fontSize = 14.sp,
        color = Black900,
    ),
    labelLarge = TextStyle(
        fontSize = 16.sp,
        color = Black900,
    ),
)
