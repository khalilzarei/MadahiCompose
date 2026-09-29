// ui/poems/PremiumDialog.kt
package com.khz.madahi.ui.poems

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.khz.madahi.helper.SUPPORT_PHONE_NUMBER
import com.khz.madahi.helper.openSupportContact
import com.khz.madahi.ui.common.DialogFullscreenWindow
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.components.ThreeDButton
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary

@Composable
fun PremiumDialog(onDismiss: () -> Unit) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = true
            )
        ) {
            DialogFullscreenWindow()

            val context = LocalContext.current
            val colors = LocalMadahiColors.current

            GlassCard3D(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 24.dp,
                            vertical = 28.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "⭐",
                        fontSize = 44.sp
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "اشتراک ویژه",
                        color = colors.textPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "با تهیه اشتراک، به این امکانات دسترسی دارید:",
                        color = colors.textMuted,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(20.dp))

                    PremiumFeatures()

                    Spacer(Modifier.height(20.dp))
                    Text(
                        text = "برای فعال‌سازی اشتراک، کافی است با پشتیبانی تماس بگیرید.",
                        color = colors.textPrimary,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(14.dp))
                    ThreeDButton(
                        text = "تماس با پشتیبانی  $SUPPORT_PHONE_NUMBER",
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { openSupportContact(context) })
                    Spacer(Modifier.height(10.dp))
                    ThreeDButton(
                        text = "بستن",
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onDismiss
                    )
                }
            }
        }
    }
}

@Composable
fun PremiumSupportButton(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    ThreeDButton(
        text = "تماس با پشتیبانی  $SUPPORT_PHONE_NUMBER",
        modifier = modifier,
        onClick = { openSupportContact(context) })
}

// ============================================================
// لیست امکانات اشتراک ویژه
// ============================================================

@Composable
fun PremiumFeatures() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        PremiumFeatureRow(
            icon = "📚",
            title = "دسترسی به کتابچه مداحی",
            subtitle = "مشاهده و جست‌وجو در متن‌ها و شعرهای کتابچه"
        )
        PremiumFeatureRow(
            icon = "📜",
            title = "شعرها همراه با سبک و صوت",
            subtitle = "مشاهده شعرهای ذخیره‌شده و پخش ویس آن‌ها"
        )
        PremiumFeatureRow(
            icon = "🎤",
            title = "افزودن ویس به شعر",
            subtitle = "ضبط یا انتخاب فایل صوتی برای متن‌ها"
        )
    }
}

@Composable
private fun PremiumFeatureRow(
    icon: String,
    title: String,
    subtitle: String
) {
    val colors = LocalMadahiColors.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            fontSize = 22.sp
        )
        Spacer(Modifier.width(12.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = title,
                color = colors.textPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = colors.textMuted,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PremiumFeaturesPreview() {
    MadahiThemeGreen(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            PremiumFeatures()
        }
    }
}
