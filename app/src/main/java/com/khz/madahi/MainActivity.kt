package com.khz.madahi

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.navigation.NavGraph
import com.khz.madahi.ui.theme.MadahiThemeGreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        // فعال‌سازی Edge-to-Edge استاندارد اندروید
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        val preferencesManager = PreferencesManager(this)
        val isNightMode = preferencesManager.isNightMode

        applyTheme(isNightMode)


        setContent {
            MadahiThemeGreen(
                darkTheme = isNightMode
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    NavGraph()
                }
            }
        }
    }

    private fun applyTheme(isNightMode: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isNightMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}