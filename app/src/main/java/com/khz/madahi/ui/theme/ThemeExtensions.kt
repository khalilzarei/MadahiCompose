// ui/theme/ThemeExtensions.kt
package com.khz.madahi.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ============ Extension Functions برای دسترسی آسان به رنگ‌ها ============

@Composable
fun getPrimaryColor(): Color = MaterialTheme.colorScheme.primary

@Composable
fun getSecondaryColor(): Color = MaterialTheme.colorScheme.secondary

@Composable
fun getBackgroundColor(): Color = MaterialTheme.colorScheme.background

@Composable
fun getSurfaceColor(): Color = MaterialTheme.colorScheme.surface

@Composable
fun getOnSurfaceColor(): Color = MaterialTheme.colorScheme.onSurface


// ============ بر اساس تم ============

@Composable
fun getDividerColor(): Color = MaterialTheme.colorScheme.outline

@Composable
fun getCardBackground(): Color = MaterialTheme.colorScheme.surfaceVariant

@Composable
fun getTextPrimary(): Color = MaterialTheme.colorScheme.onBackground

@Composable
fun getTextSecondary(): Color = MaterialTheme.colorScheme.onSurfaceVariant

data class DangerDialogPalette(
    val background: Color,
    val border: Color,
    val title: Color,
    val body: Color,
    val cancelBackground: Color,
    val cancelBorder: Color,
    val cancelText: Color,
    val deleteBackground: Color,
    val deleteText: Color
)

@Composable
fun getDangerDialogColors(): DangerDialogPalette {
    val isDark = MaterialTheme.colorScheme.background == DarkBackground || MaterialTheme.colorScheme.background == DarkSurface

    return if (isDark) {
        DangerDialogPalette(
            background = DangerDialogColors.NightBackground,
            border = DangerDialogColors.NightBorder,
            title = DangerDialogColors.NightTitle,
            body = DangerDialogColors.NightBody,
            cancelBackground = DangerDialogColors.NightCancelBackground,
            cancelBorder = DangerDialogColors.NightCancelBorder,
            cancelText = DangerDialogColors.NightCancelText,
            deleteBackground = DangerDialogColors.NightDeleteBackground,
            deleteText = DangerDialogColors.NightDeleteText
        )
    } else {
        DangerDialogPalette(
            background = DangerDialogColors.DayBackground,
            border = DangerDialogColors.DayBorder,
            title = DangerDialogColors.DayTitle,
            body = DangerDialogColors.DayBody,
            cancelBackground = DangerDialogColors.DayCancelBackground,
            cancelBorder = DangerDialogColors.DayCancelBorder,
            cancelText = DangerDialogColors.DayCancelText,
            deleteBackground = DangerDialogColors.DayDeleteBackground,
            deleteText = DangerDialogColors.DayDeleteText
        )
    }
}

data class FormDialogPalette(
    val gradient: List<Color>,
    val border: Color,
    val title: Color
)

@Composable
fun getFormDialogColors(): FormDialogPalette {
    val isDark = MaterialTheme.colorScheme.background == DarkBackground || MaterialTheme.colorScheme.background == DarkSurface

    return if (isDark) {
        FormDialogPalette(
            gradient = FormDialogColors.NightGradient,
            border = FormDialogColors.NightBorder,
            title = FormDialogColors.NightTitle
        )
    } else {
        FormDialogPalette(
            gradient = FormDialogColors.DayGradient,
            border = FormDialogColors.DayBorder,
            title = FormDialogColors.DayTitle
        )
    }
}

data class OutlineGoldButtonPalette(
    val background: Color,
    val border: Color,
    val text: Color,
    val dangerBorder: Color,
    val dangerText: Color
)

@Composable
fun getOutlineGoldButtonColors(): OutlineGoldButtonPalette {
    val isDark = MaterialTheme.colorScheme.background == DarkBackground || MaterialTheme.colorScheme.background == DarkSurface

    return if (isDark) {
        OutlineGoldButtonPalette(
            background = OutlineGoldButtonColors.NightBackground,
            border = OutlineGoldButtonColors.NightBorder,
            text = OutlineGoldButtonColors.NightText,
            dangerBorder = OutlineGoldButtonColors.NightDangerBorder,
            dangerText = OutlineGoldButtonColors.NightDangerText
        )
    } else {
        OutlineGoldButtonPalette(
            background = OutlineGoldButtonColors.DayBackground,
            border = OutlineGoldButtonColors.DayBorder,
            text = OutlineGoldButtonColors.DayText,
            dangerBorder = OutlineGoldButtonColors.DayDangerBorder,
            dangerText = OutlineGoldButtonColors.DayDangerText
        )
    }
}

// ui/theme/ThemeExtensions.kt (اضافه شود)

@Composable
fun getBottomBarColors(): BottomBarPalette {
    val isDark = MaterialTheme.colorScheme.background == DarkBackground || MaterialTheme.colorScheme.background == DarkSurface

    return if (isDark) {
        BottomBarPalette(
            background = BottomBarColors.NightBackground,
            backgroundSelected = BottomBarColors.NightBackgroundSelected,
            border = BottomBarColors.NightBorder,
            selectedBackground = BottomBarColors.NightSelectedBackground,
            selectedIcon = BottomBarColors.NightSelectedIcon,
            selectedText = BottomBarColors.NightSelectedText,
            unselectedIcon = BottomBarColors.NightUnselectedIcon,
            unselectedText = BottomBarColors.NightUnselectedText
        )
    } else {
        BottomBarPalette(
            background = BottomBarColors.DayBackground,
            backgroundSelected = BottomBarColors.DayBackgroundSelected,
            border = BottomBarColors.DayBorder,
            selectedBackground = BottomBarColors.DaySelectedBackground,
            selectedIcon = BottomBarColors.DaySelectedIcon,
            selectedText = BottomBarColors.DaySelectedText,
            unselectedIcon = BottomBarColors.NightBorder,
            unselectedText = BottomBarColors.NightBorder
        )
    }
}