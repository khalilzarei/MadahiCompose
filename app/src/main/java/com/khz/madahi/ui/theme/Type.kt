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
//   (کلید باید دقیقاً همان مقداری باشد که در PreferencesManager.font ذخیره می‌شود)
// ============================================================
object FontCatalog {

    /** کلید فونت پیش‌فرض (همان DEFAULT_FONT در PreferencesManager) */
    const val DEFAULT_FONT_KEY: String = "vazir.ttf"

    /** لیست فونت‌های قابل انتخاب در تنظیمات: (کلید → نام نمایشی) */
    val availableFonts: Map<String, String> = linkedMapOf(
        DEFAULT_FONT_KEY to "وزیر (پیش‌فرض)",
        "yekan.ttf" to "یکان",
        "titr.ttf" to "تیتر (عنوان)",
        "lalezar.ttf" to "لاله‌زار (عنوان)",
        "iransans.ttf" to "ایران‌سنس",
        "estedad.ttf" to "استعداد",
        "shabnam.ttf" to "شبنم",
        "sahel.ttf" to "ساحل",
        "samim.ttf" to "صمیم",
        "tanha.ttf" to "تنها",
        "gandom.ttf" to "گندم",
        "nahid.ttf" to "ناهید",
        "parastoo.ttf" to "پرستو"
        // ─── فونت‌های آینده را اینجا اضافه کن ───
        // "mikhak.ttf" to "میخک",
    )

    /**
     * خانواده فونت بر اساس کلید انتخابی کاربر.
     * اگر کلید ناشناخته بود → فونت پیش‌فرض (وزیر).
     */
    @Composable
    fun fontFamilyFor(key: String?): FontFamily = when (key) {
        DEFAULT_FONT_KEY -> vazirFontFamily()
        "yekan.ttf"      -> yekanFontFamily()
        "titr.ttf"       -> titrFontFamily()
        "lalezar.ttf"    -> lalezarFontFamily()
        "iransans.ttf"   -> iransansFontFamily()
        "estedad.ttf"    -> estedadFontFamily()
        "shabnam.ttf"    -> shabnamFontFamily()
        "sahel.ttf"      -> sahelFontFamily()
        "samim.ttf"      -> samimFontFamily()
        "tanha.ttf"      -> tanhaFontFamily()
        "gandom.ttf"     -> gandomFontFamily()
        "nahid.ttf"      -> nahidFontFamily()
        "parastoo.ttf"   -> parastooFontFamily()
        else             -> vazirFontFamily()
    }

    // ================== وزیر (Vazirmatn) — ۴ وزن ==================
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

    // ================== یکان (Yekan) — تک‌وزن ==================
    @Composable
    fun yekanFontFamily(): FontFamily = FontFamily(
        Font(
            R.font.yekan_regular,
            FontWeight.Normal
        )
    )

    // ================== تیتر (Titr) — تک‌وزن، عنوان‌نویس ==================
    @Composable
    fun titrFontFamily(): FontFamily = FontFamily(
        Font(
            R.font.titr_regular,
            FontWeight.Normal
        )
    )

    // ================== لاله‌زار (Lalezar) — تک‌وزن، تزئینی ==================
    @Composable
    fun lalezarFontFamily(): FontFamily = FontFamily(
        Font(
            R.font.lalezar_regular,
            FontWeight.Normal
        )
    )

    // ================== ایران‌سنس (IRANSans) — ۳ وزن ==================
    @Composable
    fun iransansFontFamily(): FontFamily = FontFamily(
        Font(
            R.font.iransans_regular,
            FontWeight.Normal
        ),
        Font(
            R.font.iransans_medium,
            FontWeight.Medium
        ),
        Font(
            R.font.iransans_bold,
            FontWeight.Bold
        )
    )

    // ================== استعداد (Estedad) — ۳ وزن ==================
    @Composable
    fun estedadFontFamily(): FontFamily = FontFamily(
        Font(
            R.font.estedad_regular,
            FontWeight.Normal
        ),
        Font(
            R.font.estedad_medium,
            FontWeight.Medium
        ),
        Font(
            R.font.estedad_bold,
            FontWeight.Bold
        )
    )

    // ================== شبنم (Shabnam) — ۳ وزن ==================
    @Composable
    fun shabnamFontFamily(): FontFamily = FontFamily(
        Font(
            R.font.shabnam_regular,
            FontWeight.Normal
        ),
        Font(
            R.font.shabnam_medium,
            FontWeight.Medium
        ),
        Font(
            R.font.shabnam_bold,
            FontWeight.Bold
        )
    )

    // ================== ساحل (Sahel) — ۳ وزن ==================
    @Composable
    fun sahelFontFamily(): FontFamily = FontFamily(
        Font(
            R.font.sahel_regular,
            FontWeight.Normal
        ),
        Font(
            R.font.sahel_semibold,
            FontWeight.SemiBold
        ),
        Font(
            R.font.sahel_bold,
            FontWeight.Bold
        )
    )

    // ================== صمیم (Samim) — ۳ وزن ==================
    @Composable
    fun samimFontFamily(): FontFamily = FontFamily(
        Font(
            R.font.samim_regular,
            FontWeight.Normal
        ),
        Font(
            R.font.samim_medium,
            FontWeight.Medium
        ),
        Font(
            R.font.samim_bold,
            FontWeight.Bold
        )
    )

    // ================== تنها (Tanha) — تک‌وزن ==================
    @Composable
    fun tanhaFontFamily(): FontFamily = FontFamily(
        Font(
            R.font.tanha_regular,
            FontWeight.Normal
        )
    )

    // ================== گندم (Gandom) — تک‌وزن ==================
    @Composable
    fun gandomFontFamily(): FontFamily = FontFamily(
        Font(
            R.font.gandom_regular,
            FontWeight.Normal
        )
    )

    // ================== ناهید (Nahid) — تک‌وزن ==================
    @Composable
    fun nahidFontFamily(): FontFamily = FontFamily(
        Font(
            R.font.nahid_regular,
            FontWeight.Normal
        )
    )

    // ================== پرستو (Parastoo) — ۲ وزن ==================
    @Composable
    fun parastooFontFamily(): FontFamily = FontFamily(
        Font(
            R.font.parastoo_regular,
            FontWeight.Normal
        ),
        Font(
            R.font.parastoo_bold,
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
