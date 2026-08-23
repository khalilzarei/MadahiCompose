// ui/message/MessageDialog.kt
package com.khz.madahi.ui.message

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiTheme
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary

// ============================================================
// دیالوگ نمایش کامل پیام — طراحی شیشه‌ای و سه‌بعدی
// ------------------------------------------------------------
// - دیالوگ جمع‌وجور وسط صفحه (مثل FontDialog)
// - آیکون + عنوان + متن پیام (اسکرول‌شونده)
// - دکمه «بستن» سه‌بعدی پایین کارت
// - کلیک روی بیرون دیالوگ هم آن را می‌بندد
// ============================================================

@Composable
fun MessageDialog(
    message: MessageItemUi,
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

            MessageDialogContent(
                message = message,
                onDismiss = onDismiss
            )
        }
    }
}

// ============================================================
// UI خالص — بدون وابستگی به Context (برای Preview)
// ============================================================

@Composable
fun MessageDialogContent(
    message: MessageItemUi,
    onDismiss: () -> Unit
) {
    val colors = LocalMadahiColors.current

    GlassCard3D(
        modifier = Modifier
            .fillMaxWidth(0.86f)
            .fillMaxHeight(0.5f)
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
                    text = "📩",
                    fontSize = 42.sp
                )


                Spacer(Modifier.width(12.dp))

                // ============ عنوان ============
                Text(
                    text = message.title,
                    color = colors.textPrimary,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(16.dp))

            // ============ متن پیام (اسکرول‌شونده) ============
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 480.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = message.description,
                    color = colors.textMuted,
                    fontSize = 15.sp,
                    lineHeight = 28.sp
                )
            }

//            Spacer(Modifier.height(22.dp))
//
//            // ============ دکمه بستن ============
//            Gold3DButton(
//                text = "بستن",
//                modifier = Modifier.fillMaxWidth(),
//                onClick = onDismiss
//            )
        }
    }
}

// ============================================================
// Preview
// ============================================================

@Preview(showBackground = true)
@Composable
fun MessageDialogContentPreviewDark() {
    MadahiTheme(darkTheme = true) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            MessageDialogContent(
                message = MessageItemUi(
                    id = "1",
                    title = "به‌روزرسانی نسخه ۱.۷",
                    description = "مجموعه نوحه‌های مناسبتی محرم و صفر به بخش محتوا اضافه شد. از شنیدن نظرات شما خوشحال می‌شویم. در صورت مشاهده هرگونه مشکل، لطفاً از بخش پیام‌ها اطلاع دهید."
                ),
                onDismiss = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessageDialogContentPreviewLight() {
    MadahiTheme(darkTheme = false) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            MessageDialogContent(
                message = MessageItemUi(
                    id = "2",
                    title = "نماینده مداحی",
                    description = "نوحه‌های جدید حاج محمود کریمی با کیفیت بالا بارگذاری شد."
                ),
                onDismiss = {})
        }
    }
}
