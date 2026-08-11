// ui/theme/Theme.kt
package com.khz.madahi.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.sp
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

    CompositionLocalProvider(
        LocalPreferences provides preferences,
        LocalLayoutDirection provides LayoutDirection.Rtl  // ✅ راست‌چین
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = MadahiTypography,
            content = content
        )
    }
}

// ============ Typography ============
val MadahiTypography = Typography(
    displayLarge = androidx.compose.material3.Typography().displayLarge.copy(
        fontSize = 57.sp,
        lineHeight = 64.sp,
        fontWeight = FontWeight.Bold,
        color = PrimaryGreen
    ),
    displayMedium = androidx.compose.material3.Typography().displayMedium.copy(
        fontSize = 45.sp,
        lineHeight = 52.sp,
        fontWeight = FontWeight.Bold,
        color = PrimaryGreen
    ),
    displaySmall = androidx.compose.material3.Typography().displaySmall.copy(
        fontSize = 36.sp,
        lineHeight = 44.sp,
        fontWeight = FontWeight.Bold,
        color = PrimaryGreen
    ),
    headlineLarge = androidx.compose.material3.Typography().headlineLarge.copy(
        fontSize = 32.sp,
        lineHeight = 40.sp,
        fontWeight = FontWeight.Bold
    ),
    headlineMedium = androidx.compose.material3.Typography().headlineMedium.copy(
        fontSize = 28.sp,
        lineHeight = 36.sp,
        fontWeight = FontWeight.Bold,
        color = PrimaryGreen
    ),
    headlineSmall = androidx.compose.material3.Typography().headlineSmall.copy(
        fontSize = 24.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight.SemiBold
    ),
    titleLarge = androidx.compose.material3.Typography().titleLarge.copy(
        fontSize = 22.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Bold,
        color = PrimaryGreen
    ),
    titleMedium = androidx.compose.material3.Typography().titleMedium.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Medium
    ),
    titleSmall = androidx.compose.material3.Typography().titleSmall.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Medium
    ),
    bodyLarge = androidx.compose.material3.Typography().bodyLarge.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = androidx.compose.material3.Typography().bodyMedium.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodySmall = androidx.compose.material3.Typography().bodySmall.copy(
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelLarge = androidx.compose.material3.Typography().labelLarge.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Medium
    ),
    labelMedium = androidx.compose.material3.Typography().labelMedium.copy(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Medium
    ),
    labelSmall = androidx.compose.material3.Typography().labelSmall.copy(
        fontSize = 11.sp,
        lineHeight = 16.sp
    )
)