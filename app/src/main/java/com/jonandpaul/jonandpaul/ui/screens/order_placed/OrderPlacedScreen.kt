package com.jonandpaul.jonandpaul.ui.screens.order_placed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.jonandpaul.jonandpaul.ui.theme.JonAndPaulTheme
import com.jonandpaul.jonandpaul.ui.utils.Constants
import com.jonandpaul.jonandpaul.R
import com.jonandpaul.jonandpaul.ui.utils.Screens
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import kotlinx.coroutines.delay

@Composable
fun OrderPlacedScreen(
    onNavigate: (UiEvent.Navigate) -> Unit
) {

    LaunchedEffect(key1 = true) {
        delay(3000L)
        onNavigate(UiEvent.Navigate(route = Screens.Home.route))
    }

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.Url(Constants.ORDER_PLACED_LOTTIE_URL))
    val progress by animateLottieCompositionAsState(
        composition = composition,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.Center)
            .background(MaterialTheme.colors.background)
    ) {
        LottieAnimation(composition = composition, progress = progress)
    }
}