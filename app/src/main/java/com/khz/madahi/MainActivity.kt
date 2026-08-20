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
        // Full Screen / Edge To Edge
        // =====================================================

        setupFullscreen()

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
    // Fullscreen
    // =========================================================

    private fun setupFullscreen() {

        /*
         * اجازه می‌دهیم Compose تمام فضای Window
         * را در اختیار داشته باشد.
         */

        WindowCompat.setDecorFitsSystemWindows(
            window,
            false
        )

        /*
         * کنترل System Barها
         */

        val controller = WindowInsetsControllerCompat(
            window,
            window.decorView
        )

        controller.hide(
            WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars()
        )

        /*
         * اگر کاربر از لبه صفحه Swipe کند،
         * System Barها موقتاً نمایش داده می‌شوند.
         */

        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
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