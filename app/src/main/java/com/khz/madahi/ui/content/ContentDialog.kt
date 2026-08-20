// ui/content/ContentDialog.kt
package com.khz.madahi.ui.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import com.khz.madahi.ui.components.GlassTextField
import com.khz.madahi.ui.components.Gold3DButton
import com.khz.madahi.ui.components.ThreeDButton
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiTheme
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.primaryLight
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary
import com.khz.madahi.ui.theme.textSecondary

// ============================================================
// دیالوگ افزودن/ویرایش محتوا — طراحی شیشه‌ای و سه‌بعدی
// امضا دقیقاً مثل نسخه قبلی است
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

    GlassCard3D(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(vertical = 24.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 22.dp,
                    vertical = 24.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ============ عنوان دیالوگ ============
            Text(
                text = if (isEditMode) "✏️ ویرایش محتوا" else "📝 محتوای جدید",
                color = colors.textPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(18.dp))

            // ============ انتخاب نوع محتوا ============
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                FilterChip(
                    selected = isNoheh,
                    onClick = { onContentTypeChange(true) },
                    label = {
                        Text(
                            text = "🕌 نوحه",
                            color = if (isNoheh) colors.gold else colors.textMuted
                        )
                    },
                    modifier = Modifier.weight(1f),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = colors.gold.copy(alpha = 0.18f),
                        selectedLabelColor = colors.gold
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isNoheh,
                        borderColor = colors.gold.copy(alpha = 0.5f),
                        selectedBorderColor = colors.gold
                    )
                )

                FilterChip(
                    selected = !isNoheh,
                    onClick = { onContentTypeChange(false) },
                    label = {
                        Text(
                            text = "🕋 روضه",
                            color = if (!isNoheh) colors.primaryLight else colors.textMuted
                        )
                    },
                    modifier = Modifier.weight(1f),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = colors.primaryLight.copy(alpha = 0.18f),
                        selectedLabelColor = colors.primaryLight
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = !isNoheh,
                        borderColor = colors.primaryLight.copy(alpha = 0.5f),
                        selectedBorderColor = colors.primaryLight
                    )
                )
            }

            Spacer(Modifier.height(18.dp))

            // ============ عنوان ============
            Text(
                text = "عنوان",
                color = colors.textSecondary,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp)
            )

            Spacer(Modifier.height(8.dp))

            GlassTextField(
                value = subject,
                onValueChange = onSubjectChange,
                minHeight = 52.dp
            )

            Spacer(Modifier.height(14.dp))

            // ============ جواب (اختیاری) ============
            Text(
                text = "جواب (اختیاری)",
                color = colors.textSecondary,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp)
            )

            Spacer(Modifier.height(8.dp))

            GlassTextField(
                value = answer,
                onValueChange = onAnswerChange,
                minHeight = 52.dp
            )

            Spacer(Modifier.height(14.dp))

            // ============ متن اصلی ============
            Text(
                text = "متن",
                color = colors.textSecondary,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp)
            )

            Spacer(Modifier.height(8.dp))

            GlassTextField(
                value = contentText,
                onValueChange = onContentChange,
                minHeight = 120.dp
            )

            Spacer(Modifier.height(24.dp))

            // ============ دکمه‌ها ============
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                ThreeDButton(
                    text = "انصراف",
                    modifier = Modifier.weight(1f),
                    onClick = onDismiss
                )

                Gold3DButton(
                    text = if (isEditMode) "ذخیره" else "افزودن",
                    modifier = Modifier.weight(1f),
                    onClick = onConfirm
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContentDialogPreview() {
    MadahiTheme(darkTheme = true) {
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