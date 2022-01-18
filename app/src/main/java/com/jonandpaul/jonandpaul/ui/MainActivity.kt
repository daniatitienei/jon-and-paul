package com.jonandpaul.jonandpaul.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import com.jonandpaul.jonandpaul.ui.theme.JonAndPaulTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jonandpaul.jonandpaul.ui.screens.home.HomeScreen
import com.squareup.moshi.Moshi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@AndroidEntryPoint
@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {

    @Inject lateinit var moshi: Moshi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val systemUiController = rememberSystemUiController()

            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = Color.White,
                    darkIcons = true
                )
            }

            JonAndPaulTheme {
                Navigation(moshi = moshi)
            }
        }
    }
}

