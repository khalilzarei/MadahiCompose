// ui/content/ContentDialog.kt
package com.khz.madahi.ui.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
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
import com.khz.madahi.ui.components.GlassTextField
import com.khz.madahi.ui.components.Gold3DButton
import com.khz.madahi.ui.components.MadahiBackground
import com.khz.madahi.ui.components.ThreeDButton
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiTheme
import com.khz.madahi.ui.theme.border
import com.khz.madahi.ui.theme.surfaceGlass
import com.khz.madahi.ui.theme.textPrimary
import com.khz.madahi.ui.theme.textSecondary

// ============================================================
// دیالوگ افزودن/ویرایش محتوا — تمام‌صفحه (Full Screen)
// ------------------------------------------------------------
// - کل صفحه را می‌پوشاند (با MadahiBackground)
// - دکمه‌ها ثابت پایین، فرم اسکرول‌شونده در وسط
// - امضا دقیقاً مثل نسخه قبلی است
// ============================================================

@Composable
fun ContentDialog(
    subject: String,
    answer: String,
    contentText: String,
    isNoheh: Boolean,
    isEditMode: Boolean,
    onSubjectChange: (String) -> Unit,
    onAnswerChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onContentTypeChange: (Boolean) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            // ✅ فول‌اسکرین ماندن window دیالوگ
            DialogFullscreenWindow()

            ContentDialogContent(
                subject = subject,
                answer = answer,
                contentText = contentText,
                isNoheh = isNoheh,
                isEditMode = isEditMode,
                onSubjectChange = onSubjectChange,
                onAnswerChange = onAnswerChange,
                onContentChange = onContentChange,
                onContentTypeChange = onContentTypeChange,
                onDismiss = onDismiss,
                onConfirm = onConfirm
            )
        }
    }
}

@Composable
fun ContentDialogContent(
    subject: String,
    answer: String,
    contentText: String,
    isNoheh: Boolean,
    isEditMode: Boolean,
    onSubjectChange: (String) -> Unit,
    onAnswerChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onContentTypeChange: (Boolean) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val colors = LocalMadahiColors.current

    // ============ پس‌زمینه تمام‌صفحه ============
    MadahiBackground {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {

            // ============ عنوان دیالوگ ============
            Text(
                text = if (isEditMode) "✏️ ویرایش محتوا" else "📝 محتوای جدید",
                color = colors.textPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
//                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            // ============ فرم (اسکرول‌شونده) ============
            GlassCard3D(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 22.dp,
                            vertical = 24.dp
                        )
                ) {

                    // عنوان
                    Text(
                        text = "عنوان",
                        color = colors.textSecondary,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp)
                    )
                    GlassTextField(
                        value = subject,
                        onValueChange = onSubjectChange,
                        minHeight = 52.dp
                    )

                    Spacer(Modifier.height(10.dp))

                    // جواب (اختیاری)
                    Text(
                        text = "جواب",
                        color = colors.textSecondary,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp)
                    )
                    GlassTextField(
                        value = answer,
                        onValueChange = onAnswerChange,
                        minHeight = 52.dp
                    )

                    Spacer(Modifier.height(10.dp))

                    // متن اصلی
                    Text(
                        text = "متن",
                        color = colors.textSecondary,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                colors.surfaceGlass.copy(alpha = 0.55f),
                                RoundedCornerShape(18.dp)
                            )
                            .border(
                                1.dp,
                                colors.border,
                                RoundedCornerShape(18.dp)
                            )
                            .weight(1f),
                    ) {

                        BasicTextField(
                            value = contentText,
                            onValueChange = onContentChange,
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .background(
                                    colors.surfaceGlass.copy(alpha = 0.55f),
                                    RoundedCornerShape(18.dp)
                                )
                                .padding(14.dp),
                            textStyle = TextStyle(
                                color = colors.textPrimary,
                                fontSize = 17.sp,
                                lineHeight = 30.sp,
                                textAlign = TextAlign.Center
                            )
                        )

                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ============ دکمه‌ها — ثابت پایین ============
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Gold3DButton(
                    text = "انصراف",
                    modifier = Modifier.weight(1f),
                    onClick = onDismiss
                )

                ThreeDButton(
                    text = if (isEditMode) "ذخیره" else "افزودن",
                    modifier = Modifier.weight(1f),
                    onClick = onConfirm
                )
            }

            Spacer(Modifier.height(6.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContentDialogPreview() {
    MadahiTheme(darkTheme = true) {
        Box(modifier = Modifier.fillMaxSize()) {
            ContentDialogContent(
                subject = "subject",
                answer = "answer",
                contentText = "<p>متن نمونه برای نمایش در صفحه جزئیات محتوا. این متن برای تست و نمایش ظاهر صفحه استفاده می‌شود.</p>",
                isNoheh = false,
                isEditMode = true,
                onSubjectChange = {},
                onAnswerChange = {},
                onContentChange = {},
                onContentTypeChange = {},
                onDismiss = {},
                onConfirm = {},
            )
        }
    }
}
