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
        fontSize = 30.sp
    ),
    h5 = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.W600,
        fontSize = 24.sp
    ),
    h6 = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.W600,
        fontSize = 20.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    body1 = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontFamily = Lato,
        fontSize = 14.sp
    ),
    button = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    overline = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.W500,
        fontSize = 12.sp
    )
)