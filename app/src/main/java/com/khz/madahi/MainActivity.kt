package com.khz.madahi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.navigation.NavGraph
import com.khz.madahi.ui.theme.MadahiTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // ✅ Splash Screen نصب
        installSplashScreen()

        super.onCreate(savedInstanceState)

        // ✅ فعال‌سازی دارک مد بر اساس Preferences
        applyTheme()

        setContent {
            MadahiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph()
                }
            }
        }
    }

    // ============ متد اعمال Theme ============
    private fun applyTheme() {
        val preferencesManager = PreferencesManager(this)
        val isNightMode = preferencesManager.isNightMode

        if (!isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}