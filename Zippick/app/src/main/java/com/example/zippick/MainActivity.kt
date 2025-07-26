package com.example.zippick

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.zippick.ui.composable.MainScreenWithBottomNav
import com.example.zippick.ui.screen.SignUpScreen
import com.example.zippick.ui.theme.AndroidLabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AndroidLabTheme {
                MainScreenWithBottomNav()
            }
        }
    }
}
