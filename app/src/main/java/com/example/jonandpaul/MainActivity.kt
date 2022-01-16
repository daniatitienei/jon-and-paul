package com.example.jonandpaul

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.example.jonandpaul.ui.theme.JonAndPaulTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
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
                Scaffold(backgroundColor = MaterialTheme.colors.background) {
                    Text(text = "Test")
                }
            }
        }
    }
}

