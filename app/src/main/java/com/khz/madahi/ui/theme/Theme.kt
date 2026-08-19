// ui/theme/Theme.kt
package com.khz.madahi.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.khz.madahi.data.local.preferences.PreferencesManager

// ============================================================
// Preferences
// ============================================================

val LocalPreferences = compositionLocalOf<PreferencesManager> {
    error("PreferencesManager not provided")
}

// ============================================================
// Local ColorScheme
// ============================================================

val LocalMadahiColors = staticCompositionLocalOf<ColorScheme> {
    LightMadahiColorsScheme
}

// ============================================================
// Theme اصلی
// ============================================================

@Composable
fun MadahiThemeGreen(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val preferences = remember {
        PreferencesManager(context)
    }

    /*
     * اگر کاربر در تنظیمات حالت شب را فعال کرده باشد،
     * DarkTheme اولویت دارد.
     *
     * در غیر این صورت از darkTheme استفاده می‌کنیم.
     */
    val isDark = preferences.isNightMode || darkTheme

    val colors = if (isDark) {
        DarkMadahiColorsScheme
    } else {
        LightMadahiColorsScheme
    }

    val fontFamily = FontCatalog.fontFamilyFor(
        preferences.font
    )

    CompositionLocalProvider(
        LocalPreferences provides preferences,
        LocalMadahiColors provides colors,
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        MaterialTheme(
            colorScheme = colors,
            typography = madahiTypography(fontFamily),
            content = content
        )
    }
}

// ============================================================
// Theme قدیمی
// برای سازگاری با کدهای قبلی پروژه
// ============================================================

@Composable
fun MadahiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MadahiThemeGreen(
        darkTheme = darkTheme,
        content = content
    )
}

// ============================================================
// Dark Color Scheme
// ============================================================

val DarkMadahiColorsScheme: ColorScheme = darkColorScheme(

    // --------------------------------------------------------
    // Primary
    // --------------------------------------------------------

    primary = Color(0xFF3F8F4D),
    onPrimary = Color(0xFFF9FAF6),

    primaryContainer = Color(0xFF1D5B31),
    onPrimaryContainer = Color(0xFFB8E6B9),

    // --------------------------------------------------------
    // Secondary
    // --------------------------------------------------------

    secondary = Color(0xFF2E7040),
    onSecondary = Color(0xFFF9FAF6),

    secondaryContainer = Color(0xFF193024),
    onSecondaryContainer = Color(0xFFB9D7BD),

    // --------------------------------------------------------
    // Background
    // --------------------------------------------------------

    background = Color(0xFF07140D),
    onBackground = Color(0xFFF5F2E8),

    // --------------------------------------------------------
    // Surface
    // --------------------------------------------------------

    surface = Color(0xFF0C1F15),
    onSurface = Color(0xFFF5F2E8),

    surfaceVariant = Color(0xFF193024),
    onSurfaceVariant = Color(0xFFC5CCBF),

    // --------------------------------------------------------
    // Error
    // --------------------------------------------------------

    error = Color(0xFFC85C52),
    onError = Color.White,

    errorContainer = Color(0xFF5A211D),
    onErrorContainer = Color(0xFFFFDAD6),

    // --------------------------------------------------------
    // Tertiary
    // برای رنگ طلایی
    // --------------------------------------------------------

    tertiary = Color(0xFFC6A75E),
    onTertiary = Color(0xFF17120A),

    tertiaryContainer = Color(0xFF3D3118),
    onTertiaryContainer = Color(0xFFE5CB83),

    // --------------------------------------------------------
    // Border / Outline
    // --------------------------------------------------------

    outline = Color(0xFF55735D),
    outlineVariant = Color(0xFF3B5141),

    // --------------------------------------------------------
    // Other
    // --------------------------------------------------------

    scrim = Color.Black.copy(alpha = 0.7f),

    inverseSurface = Color(0xFFE8EDE6),
    inverseOnSurface = Color(0xFF182019),

    inversePrimary = Color(0xFF78C77D)
)

// ============================================================
// Light Color Scheme
// ============================================================

// ============================================================
// Light Color Scheme
// Glass + 3D Green / Gold
// ============================================================

val LightMadahiColorsScheme: ColorScheme = lightColorScheme(

    // --------------------------------------------------------
    // Primary
    // --------------------------------------------------------
    primary = Color(0xFF34763D),
    onPrimary = Color(0xFFFFFFFF),

    primaryContainer = Color(0xFFD0E4D0),
    onPrimaryContainer = Color(0xFF1F4F28),


    // --------------------------------------------------------
    // Secondary
    // --------------------------------------------------------
    secondary = Color(0xFF4F8057),
    onSecondary = Color(0xFFFFFFFF),

    secondaryContainer = Color(0xFFD9E8D9),
    onSecondaryContainer = Color(0xFF214A29),


    // --------------------------------------------------------
    // Background
    //
    // عمداً سفید نیست.
    // این رنگ باعث می‌شود GlassCard از صفحه جدا شود.
    // --------------------------------------------------------
    background = Color(0xFFE4EBE3),
    onBackground = Color(0xFF18221A),


    // --------------------------------------------------------
    // Surface
    //
    // سطح اصلی Glass
    // --------------------------------------------------------
    surface = Color(0xFFF7FAF6),
    onSurface = Color(0xFF18221A),


    // --------------------------------------------------------
    // Surface Variant
    //
    // بخش تیره‌تر Glass
    // برای ایجاد عمق سه‌بعدی
    // --------------------------------------------------------
    surfaceVariant = Color(0xFFDCE6DC),
    onSurfaceVariant = Color(0xFF526054),


    // --------------------------------------------------------
    // Error
    // --------------------------------------------------------
    error = Color(0xFFB74C44),
    onError = Color.White,

    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF5A1B16),


    // --------------------------------------------------------
    // Tertiary
    //
    // Gold
    // --------------------------------------------------------
    tertiary = Color(0xFFA77C2E),
    onTertiary = Color(0xFFFFFFFF),

    tertiaryContainer = Color(0xFFF0DFB5),
    onTertiaryContainer = Color(0xFF49360A),


    // --------------------------------------------------------
    // Outline
    //
    // Border شیشه‌ای
    // --------------------------------------------------------
    outline = Color(0xFF9FB2A1),
    outlineVariant = Color(0xFFC7D2C7),


    // --------------------------------------------------------
    // Scrim
    //
    // Shadow
    // --------------------------------------------------------
    scrim = Color.Black.copy(alpha = 0.38f),


    // --------------------------------------------------------
    // Inverse
    // --------------------------------------------------------
    inverseSurface = Color(0xFF0C1F15),
    inverseOnSurface = Color(0xFFF5F2E8),

    inversePrimary = Color(0xFF78C77D)
)

// ============================================================
// Madahi Custom Colors
// ============================================================

val ColorScheme.textPrimary: Color
    get() = onBackground

val ColorScheme.textSecondary: Color
    get() = onTertiaryContainer

val ColorScheme.textMuted: Color
    get() = onSurfaceVariant.copy(alpha = 0.65f)

val ColorScheme.primaryLight: Color
    get() = inversePrimary

val ColorScheme.primaryDark: Color
    get() = primaryContainer

val ColorScheme.gold: Color
    get() = tertiary

val ColorScheme.goldLight: Color
    get() = onTertiaryContainer

val ColorScheme.border: Color
    get() = outline

val ColorScheme.borderLight: Color
    get() = outlineVariant

val ColorScheme.surfaceGlass: Color
    get() = surfaceVariant

val ColorScheme.surfaceGlassLight: Color
    get() = surface

val ColorScheme.success: Color
    get() = Color(0xFF4E9654)

val ColorScheme.danger: Color
    get() = error

val ColorScheme.buttonText: Color
    get() = onPrimary

val ColorScheme.shadow: Color
    get() = scrim