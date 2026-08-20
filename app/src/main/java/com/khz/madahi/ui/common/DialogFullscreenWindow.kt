// ui/common/DialogFullscreenWindow.kt
package com.khz.madahi.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * داخل هر Dialog (یا AlertDialog) صدا بزن تا window دیالوگ هم
 * فول‌اسکرین بماند. بدون این، باز شدن دیالوگ نوارهای سیستم را
 * دوباره نشان می‌دهد و بعد از بستن هم گاهی برنمی‌گردند.
 *
 * استفاده:
 *   Dialog(...) {
 *       DialogFullscreenWindow()   // ← فقط همین یک خط
 *       ...
 *   }
 */
@Composable
fun DialogFullscreenWindow() {
    val view = LocalView.current

    SideEffect {
        // parent ویوِ ریشه‌ی دیالوگ، window آن دیالوگ است
        val window = (view.parent as? DialogWindowProvider)?.window
                ?: return@SideEffect

        // دیالوگ هم edge-to-edge شود
        WindowCompat.setDecorFitsSystemWindows(
            window,
            false
        )

        // نوارهای سیستم داخل window دیالوگ مخفی شوند
        WindowInsetsControllerCompat(
            window,
            view
        ).apply {
            hide(
                WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars()
            )
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}
