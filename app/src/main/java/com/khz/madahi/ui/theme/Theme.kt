// ui/theme/Theme.kt
package com.khz.madahi.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.khz.madahi.data.local.preferences.PreferencesManager

// ============ CompositionLocal ============
val LocalPreferences = compositionLocalOf<PreferencesManager> {
    error("PreferencesManager not provided")
}

// ============ تابع Theme ============
@Composable
fun MadahiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val preferences = remember { PreferencesManager(context) }
    val isDark = if (preferences.isNightMode) true else darkTheme

    val colorScheme = if (isDark) {

        darkColorScheme(
            primary = PrimaryGreen,
            onPrimary = Color.White,
            primaryContainer = DarkBrown,
            onPrimaryContainer = SecondaryGreenLight,
            secondary = SecondaryGreen,
            onSecondary = Color.White,
            secondaryContainer = DarkGreen,
            onSecondaryContainer = SecondaryGreenLight,
            background = NightColors.Background,
            onBackground = NightColors.TextPrimary,
            surface = NightColors.Surface,
            onSurface = NightColors.TextPrimary,
            surfaceVariant = NightColors.SurfaceVariant,
            onSurfaceVariant = NightColors.TextHint,
            error = Error,
            onError = Color.White,
            tertiary = PrimaryGreenLight,
            onTertiary = NightColors.Background,
            tertiaryContainer = NightColors.SurfaceVariant2,
            onTertiaryContainer = PrimaryGreenLight,
            outline = NightColors.Divider,
            scrim = Color.Black.copy(alpha = 0.7f),
            inverseSurface = NightColors.SurfaceVariant,
            inverseOnSurface = NightColors.TextPrimary,
        )
    } else {
        lightColorScheme(
            primary = PrimaryGreen,
            onPrimary = Color.White,
            primaryContainer = DayColors.SurfaceVariant,
            onPrimaryContainer = PrimaryGreenDark,
            secondary = SecondaryGreen,
            onSecondary = Color.White,
            secondaryContainer = DayColors.SurfaceVariant,
            onSecondaryContainer = PrimaryGreenDark,
            background = DayColors.Background,
            onBackground = DayColors.TextPrimary,
            surface = DayColors.Surface,
            onSurface = DayColors.TextPrimary,
            surfaceVariant = DayColors.SurfaceVariant,
            onSurfaceVariant = DayColors.TextHint,
            error = Error,
            onError = Color.White,
            tertiary = PrimaryGreenDark,
            onTertiary = Color.White,
            tertiaryContainer = DayColors.SurfaceVariant2,
            onTertiaryContainer = PrimaryGreenDark,
            outline = DayColors.Divider,
            scrim = Color.Black.copy(alpha = 0.5f),
            inverseSurface = NightColors.Surface,
            inverseOnSurface = NightColors.TextPrimary
        )
    }

    // ============ فونت انتخابی کاربر ============
    // 🔮 با این طراحی، بعداً در تنظیمات فقط کافی است
    // preferences.font را با یکی از کلیدهای FontCatalog.availableFonts
    // عوض کنیم — همین‌جا خودکار اعمال می‌شود.
    val fontFamily = FontCatalog.fontFamilyFor(preferences.font)

    CompositionLocalProvider(
        LocalPreferences provides preferences,
        LocalLayoutDirection provides LayoutDirection.Rtl  // ✅ راست‌چین
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = madahiTypography(fontFamily),
            content = content
        )
    }
}