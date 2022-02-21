package com.jonandpaul.jonandpaul.ui.screens.order_placed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.jonandpaul.jonandpaul.ui.utils.Constants
import com.jonandpaul.jonandpaul.ui.utils.Screens
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import kotlinx.coroutines.delay

@Composable
fun OrderPlacedScreen(
    onTimeout: (UiEvent.Navigate) -> Unit
) {

    LaunchedEffect(key1 = true) {
        delay(3000L)
        onTimeout(UiEvent.Navigate(route = Screens.Home.route))
    }

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.Url(Constants.ORDER_PLACED_LOTTIE_URL))
    val progress by animateLottieCompositionAsState(
        composition = composition,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.Center)
            .background(MaterialTheme.colorScheme.background)
    ) {
        LottieAnimation(composition = composition, progress = progress)
    }
}