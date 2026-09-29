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
 * داخل هر Dialog (یا AlertDialog) صدا بزن تا دیالوگ هم نوارهای
 * وضعیت و پیمایش سیستم را نمایش دهد.
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

        // دیالوگ با درنظرگرفتن نوارهای سیستم چیده شود.
        WindowCompat.setDecorFitsSystemWindows(
            window,
            true
        )

        WindowInsetsControllerCompat(
            window,
            view
        ).apply {
            show(
                WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars()
            )
        }
    }
}
