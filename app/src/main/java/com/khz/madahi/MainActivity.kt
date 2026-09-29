package com.khz.madahi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.navigation.NavGraph
import com.khz.madahi.ui.theme.MadahiThemeGreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        // =====================================================
        // Splash Screen
        // =====================================================

        installSplashScreen()

        super.onCreate(savedInstanceState)

        // =====================================================
        // System bars
        // =====================================================

        setupSystemBars()

        // =====================================================
        // Preferences
        // =====================================================

        val preferencesManager = PreferencesManager(this)

        val isNightMode = preferencesManager.isNightMode

        // =====================================================
        // Theme
        // =====================================================

        applyTheme(isNightMode)

        // =====================================================
        // Compose
        // =====================================================

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

    // =========================================================
    // System bars
    // =========================================================

    // =====================================================
// Keep the status and navigation bars visible after dialogs or focus changes.
// =====================================================
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            setupSystemBars()
        }
    }

    private fun setupSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(
            window,
            true
        )

        val controller = WindowInsetsControllerCompat(
            window,
            window.decorView
        )
        controller.show(
            WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars()
        )
    }

    // =========================================================
    // Theme
    // =========================================================

    private fun applyTheme(
        isNightMode: Boolean
    ) {

        AppCompatDelegate.setDefaultNightMode(
            if (isNightMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
