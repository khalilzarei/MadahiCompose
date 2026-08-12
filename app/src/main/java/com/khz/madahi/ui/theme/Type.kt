// ui/theme/Type.kt
package com.khz.madahi.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.khz.madahi.R

// ============================================================
// کاتالوگ فونت‌های اپ
// ------------------------------------------------------------
// 🔮 برای افزودن فونت جدید در آینده (از صفحه تنظیمات):
//   ۱) فایل ttf را در app/src/main/res/font/ قرار بده
//   ۲) یک خط به availableFonts اضافه کن:  "کلید" to "نام نمایشی"
//   ۳) یک case به fontFamilyFor اضافه کن
//   ۴) در SettingScreen لیست انتخاب فونت را به‌روز کن
//   (کلید باید دقیقاً همان مقداری باشد که در PreferencesManager.font ذخیره می‌شود)
// ============================================================
object FontCatalog {

    /** کلید فونت پیش‌فرض (همان DEFAULT_FONT در PreferencesManager) */
    const val DEFAULT_FONT_KEY: String = "vazir.ttf"

    /** لیست فونت‌های قابل انتخاب در تنظیمات: (کلید → نام نمایشی) */
    val availableFonts: Map<String, String> = linkedMapOf(
        DEFAULT_FONT_KEY to "وزیر (پیش‌فرض)"
        // ─── فونت‌های آینده را اینجا اضافه کن ───
        // "iransans.ttf" to "ایران‌سنس",
        // "lalezar.ttf"  to "لاله‌زار",
        // "nastaliq.ttf" to "نستعلیق",
    )

    /**
     * خانواده فونت بر اساس کلید انتخابی کاربر.
     * اگر کلید ناشناخته بود → فونت پیش‌فرض (وزیر).
     */
    @Composable
    fun fontFamilyFor(key: String?): FontFamily = when (key) {
        DEFAULT_FONT_KEY -> vazirFontFamily()
        // ─── فونت‌های آینده را اینجا اضافه کن ───
        // "iransans.ttf" -> iransansFontFamily()
        else             -> vazirFontFamily()
    }

    // ================== فونت وزیر (Vazirmatn) ==================
    @Composable
    fun vazirFontFamily(): FontFamily = FontFamily(
        Font(
            R.font.vazir_regular,
            FontWeight.Normal
        ),
        Font(
            R.font.vazir_medium,
            FontWeight.Medium
        ),
        Font(
            R.font.vazir_semibold,
            FontWeight.SemiBold
        ),
        Font(
            R.font.vazir_bold,
            FontWeight.Bold
        )
    )
}

// ============================================================
// تایپوگرافی کامل اپ — با فونت انتخابی کاربر
// letterSpacing همه استایل‌ها 0 است چون در متن فارسی
// فاصله‌گذاری حروف، حروفچینی را خراب می‌کند.
// ============================================================
@Composable
fun madahiTypography(fontFamily: FontFamily): Typography {

    return Typography(
        displayLarge = Typography().displayLarge.copy(
            fontFamily = fontFamily,
            fontSize = 57.sp,
            lineHeight = 64.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen,
            letterSpacing = 0.sp
        ),
        displayMedium = Typography().displayMedium.copy(
            fontFamily = fontFamily,
            fontSize = 45.sp,
            lineHeight = 52.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen,
            letterSpacing = 0.sp
        ),
        displaySmall = Typography().displaySmall.copy(
            fontFamily = fontFamily,
            fontSize = 36.sp,
            lineHeight = 44.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen,
            letterSpacing = 0.sp
        ),
        headlineLarge = Typography().headlineLarge.copy(
            fontFamily = fontFamily,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.sp
        ),
        headlineMedium = Typography().headlineMedium.copy(
            fontFamily = fontFamily,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen,
            letterSpacing = 0.sp
        ),
        headlineSmall = Typography().headlineSmall.copy(
            fontFamily = fontFamily,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.sp
        ),
        titleLarge = Typography().titleLarge.copy(
            fontFamily = fontFamily,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen,
            letterSpacing = 0.sp
        ),
        titleMedium = Typography().titleMedium.copy(
            fontFamily = fontFamily,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.sp
        ),
        titleSmall = Typography().titleSmall.copy(
            fontFamily = fontFamily,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.sp
        ),
        bodyLarge = Typography().bodyLarge.copy(
            fontFamily = fontFamily,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp
        ),
        bodyMedium = Typography().bodyMedium.copy(
            fontFamily = fontFamily,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.sp
        ),
        bodySmall = Typography().bodySmall.copy(
            fontFamily = fontFamily,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.sp
        ),
        labelLarge = Typography().labelLarge.copy(
            fontFamily = fontFamily,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.sp
        ),
        labelMedium = Typography().labelMedium.copy(
            fontFamily = fontFamily,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.sp
        ),
        labelSmall = Typography().labelSmall.copy(
            fontFamily = fontFamily,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.sp
        )
    )
}