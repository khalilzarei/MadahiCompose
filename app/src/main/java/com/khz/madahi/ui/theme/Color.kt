// ui/theme/Color.kt
package com.khz.madahi.ui.theme

import androidx.compose.ui.graphics.Color

object RibbonButtonColors {
    val Border = Color(0xFFD4AF37)
    val Ornament = Color(0xFFD4AF37)

    // سبز (ذخیره/تایید)
    val SaveGradient = listOf(
        Color(0xFF1E6B3A),
        Color(0xFF0F3D20)
    )

    // قرمز (حذف)
    val DeleteGradient = listOf(
        Color(0xFFA13232),
        Color(0xFF6B1B1B)
    )

    val TextLight = Color(0xFFF5EFE0)
}


object OutlineGoldButtonColors {

    // ---------- تم شب ----------
    val NightBackground = Color(0xFF161616)
    val NightBorder = Color(0xFFD4AF37)
    val NightText = Color(0xFFD4AF37)

    // ---------- تم روز ----------
    val DayBackground = Color(0xFFFFFDF7)
    val DayBorder = Color(0xFFD9A441)
    val DayText = Color(0xFF8A5A12)

    // ---------- حالت خطر (حذف) ----------
    val NightDangerBorder = Color(0xFFB33A3A)
    val NightDangerText = Color(0xFFE05C5C)

    val DayDangerBorder = Color(0xFFB33A3A)
    val DayDangerText = Color(0xFFB33A3A)
}

object FormDialogColors {

    // ---------- تم شب ----------
    val NightGradient = listOf(
        Color(0xFF0D1A0D),
        Color(0xFF1F3A1F),
        Color(0xFF0D1A0D)
    )
    val NightBorder = Color(0xFFD4AF37)
    val NightTitle = PrimaryGreen

    // ---------- تم روز ----------
    val DayGradient = listOf(
        Color(0xFFFFFDF7),
        Color(0xFFF3E9D2),
        Color(0xFFFFFDF7)
    )
    val DayBorder = Color(0xFFD9A441)
    val DayTitle = PrimaryGreenDark
}

object DangerDialogColors {

    // ---------- تم شب ----------
    val NightBackground = Color(0xFF07130c)
    val NightBorder = Color(0xFFD4AF37)
    val NightTitle = Color(0xFFD4AF37)
    val NightBody = Color(0xFFEDEDED)
    val NightCancelBackground = PrimaryGreenDark
    val NightCancelBorder = PrimaryGreenDark
    val NightCancelText = Color.White
    val NightDeleteBackground = Color(0xFFD00000)
    val NightDeleteText = Color.White

    // ---------- تم روز ----------
    val DayBackground = Color(0xFFFBF3E3)
    val DayBorder = Color(0xFFD9A441)
    val DayTitle = Color(0xFF5C1A1A)
    val DayBody = Color(0xFF4A3524)
    val DayCancelBackground = PrimaryGreenDark
    val DayCancelBorder = PrimaryGreenDark
    val DayCancelText = Color(0xFFFBF3E3)
    val DayDeleteBackground = Color(0xFFB33A3A)
    val DayDeleteText = Color.White
}

// ============ رنگ‌های اصلی (سبز) ============
val PrimaryGreen = Color(0xFF4E9F3D)
// #4e9f3d
val PrimaryGreenDark = Color(0xFF1B5E20)    // #FF07130c
val PrimaryGreenLight = Color(0xFF66BB6A)   // #66bb6a
val SecondaryGreen = Color(0xFF388E3C)      // #388e3c
val DarkGreen = Color(0xFF388E3C)      // #388e3c
val SecondaryGreenLight = Color(0xFF81C784) // #81c784

// ============ رنگ‌های پس‌زمینه ============
val DarkBackground = Color(0xFF0B0B0B)      // #0b0b0b
val DarkSurface = Color(0xFF1A1A1A)         // #1a1a1a
val DarkBrown = Color(0xFF1A2E1A)           // #1a2e1a (سبز تیره)
val MediumBrown = Color(0xFF2E4A2E)         // #2e4a2e (سبز متوسط)
val WarmBrown = Color(0xFF4A6A4A)           // #4a6a4a (سبز روشن‌تر)

// ============ رنگ‌های متن ============
val TextPrimary = Color(0xFFFFFFFF)          // سفید
val TextSecondary = PrimaryGreenLight       // سبز روشن
val TextHint = WarmBrown                    // سبز خاکی

// ============ رنگ‌های دکمه ============
val ButtonPrimary = PrimaryGreen            // #4e9f3d
val ButtonPrimaryDark = PrimaryGreenDark    // #1b5e20
val ButtonSecondary = SecondaryGreen        // #388e3c

// ============ رنگ‌های Card و Surface ============
val CardBackground = DarkSurface            // #1a1a1a
val CardBorder = MediumBrown                // #2e4a2e

// ============ رنگ‌های وضعیت ============
val Success = PrimaryGreen                  // #4e9f3d
val Error = Color(0xFFD32F2F)               // قرمز
val Warning = Color(0xFFFFC107)             // زرد
val Gold = Color(0xFFFFC107)             // زرد

// ============ رنگ‌های آیکون ============
val IconPrimary = PrimaryGreen              // #4e9f3d
val IconSecondary = SecondaryGreen          // #388e3c

// ============ رنگ‌های Dots (Intro) ============
val DotActive = PrimaryGreen                // #4e9f3d
val DotInactive = Color(0x66FFFFFF)         // سفید با透明度

// ============ رنگ‌های Divider ============
val DividerLight = Color(0xFFE0E0E0)
val DividerDark = Color(0xFF2E4A2E)

// ============ رنگ‌های تم شب ============
object NightColors {
    val Background = Color(0xFF0B0B0B)       // #0b0b0b
    val Surface = Color(0xFF1A1A1A)          // #1a1a1a
    val SurfaceVariant = Color(0xFF1A2E1A)   // #1a2e1a
    val SurfaceVariant2 = Color(0xFF2E4A2E)  // #2e4a2e
    val TextPrimary = Color(0xFFFFFFFF)      // سفید
    val TextSecondary = SecondaryGreenLight  // #81c784
    val TextHint = WarmBrown                 // #4a6a4a
    val Divider = Color(0xFF2E4A2E)          // #2e4a2e
    val Border_Color = Gold          // #2e4a2e


}

// ============ رنگ‌های تم روز ============
object DayColors {
    val Background = Color(0xFFF5F8F0)       // سبز بسیار روشن
    val Surface = Color(0xFFFFFFFF)          // سفید
    val SurfaceVariant = Color(0xFFE8F5E9)   // سبز خیلی روشن
    val SurfaceVariant2 = Color(0xFFC8E6C9)  // سبز روشن
    val TextPrimary = Color(0xFF1A1A1A)      // مشکی
    val TextSecondary = PrimaryGreenDark     // #1b5e20
    val TextHint = Color(0xFF6B8A6B)         // سبز خاکی
    val Divider = Color(0xFFC8E6C9)          // سبز روشن

    val Border_Color = PrimaryGreenDark          // #2e4a2e
}

object BottomBarColors {
    // ---- تم شب ----
    val NightBackground = listOf(
        Color(0xFF0b0b0b),
        Color(0xFF232323),
        Color(0xFF0b0b0b)
    )
    val NightBackgroundSelected = listOf(
        Color(0xFF08210C),
        Color(0xFF0D3311),
        Color(0xFF08210C)
    )
    val NightBorder = Gold_Border_Dark
    val NightSelectedBackground = PrimaryGreen
    val NightSelectedIcon = Color.White
    val NightSelectedText = Color.White
    val NightUnselectedIcon = Gold_Border_Dark
    val NightUnselectedText = Gold_Border_Dark

    // ---- تم روز ----
    val DayBackground = listOf(
        DayColors.Surface,
        DayColors.SurfaceVariant,
        DayColors.Surface
    )
    val DayBackgroundSelected = listOf(
        DayColors.Surface,
        DayColors.SurfaceVariant,
        DayColors.Surface
    )
    val DayBorder = PrimaryGreenDark
    val DaySelectedBackground = PrimaryGreen
    val DaySelectedIcon = PrimaryGreenDark
    val DaySelectedText = PrimaryGreenDark
    val DayUnselectedIcon = PrimaryGreenDark
    val DayUnselectedText = PrimaryGreenDark
}

// رنگ طلایی بوردر (همون رنگی که در دیالوگ‌ها استفاده شده: 0xFFD4AF37)
val Gold_Border_Dark = Color(0xFFA1841C)

data class BottomBarPalette(
    val background: List<Color>,
    val backgroundSelected: List<Color>,
    val border: Color,
    val selectedBackground: Color,
    val selectedIcon: Color,
    val selectedText: Color,
    val unselectedIcon: Color,
    val unselectedText: Color
)