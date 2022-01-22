package com.jonandpaul.jonandpaul.ui.theme

import androidx.compose.material.Typography
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
    h4 = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.W600,
        fontSize = 30.sp,
        color = Black900
    ),
    h5 = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.W600,
        fontSize = 24.sp,
        color = Black900
    ),
    h6 = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.W600,
        fontSize = 20.sp,
        color = Black900
    ),
    subtitle1 = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp,
        color = Black900
    ),
    subtitle2 = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        color = Black900
    ),
    body1 = TextStyle(
        fontFamily = Lato,
        fontSize = 16.sp,
        color = Black900
    ),
    body2 = TextStyle(
        fontFamily = Lato,
        fontSize = 14.sp,
        color = Black900
    ),
    button = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
        color = Black900
    ),
    caption = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        color = Black900
    ),
    overline = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.W500,
        fontSize = 12.sp,
        color = Black900
    )
)