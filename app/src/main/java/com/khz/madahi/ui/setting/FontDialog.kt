// ui/setting/FontDialog.kt
package com.khz.madahi.ui.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.khz.madahi.ui.common.DialogFullscreenWindow
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.theme.FontCatalog
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.primaryLight
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary

// ============================================================
// دیالوگ انتخاب فونت — طراحی شیشه‌ای و سه‌بعدی
// ------------------------------------------------------------
// - دیالوگ جمع‌وجور وسط صفحه (مثل DeleteCategoryDialog)
// - لیست فونت‌ها داخل GlassCard3D اسکرول‌شونده
//   (نام هر فونت با خود فونت + جمله نمونه نمایش داده می‌شود)
// - دکمه «بستن» سه‌بعدی پایین کارت
// ============================================================

@Composable
fun FontDialog(
    currentFontKey: String,
    onFontSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            // ✅ window دیالوگ هم edge-to-edge بماند (مثل بقیه دیالوگ‌ها)
            DialogFullscreenWindow()

            FontDialogContent(
                currentFontKey = currentFontKey,
                onFontSelected = onFontSelected,
                onDismiss = onDismiss
            )
        }
    }
}

// ============================================================
// UI خالص — بدون وابستگی به Context (برای Preview)
// ============================================================

@Composable
fun FontDialogContent(
    currentFontKey: String,
    onFontSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = LocalMadahiColors.current

    GlassCard3D(
        modifier = Modifier
            .fillMaxWidth(0.80f)
            .padding(vertical = 24.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 26.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ============ آیکون ============
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "✒️",
                    fontSize = 38.sp
                )


                Spacer(Modifier.width(12.dp))

                // ============ عنوان (با فونت انتخابی فعلی) ============
                Text(
                    text = "انتخاب فونت",
                    color = colors.textPrimary,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontCatalog.fontFamilyFor(currentFontKey),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(18.dp))

            // ============ لیست فونت‌ها (اسکرول‌شونده) ============
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 480.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                FontCatalog.availableFonts.forEach { (key, label) ->
                    val isSelected = key == currentFontKey
                    val fontFamily = FontCatalog.fontFamilyFor(key)
                    // نام تمیز بدون پرانتز (مثلاً «تیتر (عنوان)» → «تیتر»)
                    val shortName = label.substringBefore("(")
                        .trim()

                    GlassCard3D(
                        modifier = Modifier.padding(vertical = 3.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = if (isSelected) {
                                        colors.primaryLight.copy(alpha = 0.16f)
                                    } else {
                                        Color.Transparent
                                    },
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable { onFontSelected(key) }
                                .padding(
                                    horizontal = 12.dp,
                                    vertical = 9.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically) {

                            // نام + جمله نمونه — هر دو با خود فونت
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = label,
                                    fontFamily = fontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = colors.textPrimary
                                )
                                Spacer(Modifier.height(3.dp))
                                Text(
                                    text = "این نمونه فونت $shortName است",
                                    fontFamily = fontFamily,
                                    fontSize = 13.sp,
                                    color = colors.textMuted
                                )
                            }

                            // ✅ تیک فونت انتخابی
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "فونت انتخابی",
                                    tint = colors.gold,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                    }

                    // فاصله بین ردیف‌ها
                    Spacer(Modifier.height(4.dp))
                }
            }

        }
    }
}

// ============================================================
// Preview
// ============================================================

@Preview(showBackground = false)
@Composable
fun FontDialogContentPreviewDark() {
    MadahiThemeGreen(darkTheme = true) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            FontDialogContent(
                currentFontKey = FontCatalog.DEFAULT_FONT_KEY,
                onFontSelected = {},
                onDismiss = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FontDialogContentPreviewLight() {
    MadahiThemeGreen(darkTheme = false) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            FontDialogContent(
                currentFontKey = "titr.ttf",
                onFontSelected = {},
                onDismiss = {})
        }
    }
}
