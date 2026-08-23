// ui/category/CategoryDialog.kt
package com.khz.madahi.ui.category

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.khz.madahi.ui.common.DialogFullscreenWindow
import com.khz.madahi.ui.components.Delete3DButton
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.components.GlassTextField
import com.khz.madahi.ui.components.Gold3DButton
import com.khz.madahi.ui.components.ThreeDButton
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiTheme
import com.khz.madahi.ui.theme.delete
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary
import com.khz.madahi.ui.theme.textSecondary

// ============================================================
// دیالوگ دسته‌بندی — طراحی شیشه‌ای و سه‌بعدی
// امضا دقیقاً مثل نسخه قبلی است (CategoryScreen دست نمی‌خورد)
// ============================================================

@Composable
fun CategoryDialog(
    title: String,
    description: String,
    isEditMode: Boolean,
    titleError: String? = null,
    descriptionError: String? = null,
    errorMessage: String? = null,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onDelete: () -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            // ✅ فول‌اسکرین ماندن هنگام باز بودن دیالوگ
            DialogFullscreenWindow()

            CategoryDialogContent(
                title = title,
                description = description,
                isEditMode = isEditMode,
                titleError = titleError,
                descriptionError = descriptionError,
                errorMessage = errorMessage,
                onTitleChange = onTitleChange,
                onDescriptionChange = onDescriptionChange,
                onConfirm = onConfirm,
                onDismiss = onDismiss,
                onDelete = onDelete
            )
        }
    }
}

// ============================================================
// محتوای دیالوگ — stateless (برای Preview هم قابل استفاده)
// ============================================================

@Composable
fun CategoryDialogContent(
    title: String,
    description: String,
    isEditMode: Boolean,
    titleError: String? = null,
    descriptionError: String? = null,
    errorMessage: String? = null,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    val colors = LocalMadahiColors.current

    GlassCard3D(
        modifier = Modifier
            .fillMaxWidth(0.86f)
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
                text = if (isEditMode) "✏️ ویرایش دسته‌بندی" else "📁 دسته‌بندی جدید",
                color = colors.textPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = if (isEditMode) "عنوان و توضیحات را ویرایش کنید" else "برای دسته‌بندی خود عنوان و توضیحات وارد کنید",
                color = colors.textMuted,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(22.dp))

            // ============ فیلد عنوان ============
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
                value = title,
                onValueChange = onTitleChange,
                minHeight = 54.dp,
                keyboardType = KeyboardType.Text,
                hasError = titleError != null
            )

            // ✅ خطای فیلد عنوان — زیر همان فیلد (مثل setError)
            if (titleError != null) {
                Text(
                    text = "⚠️ $titleError",
                    color = colors.delete,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, start = 8.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            // ============ فیلد توضیحات ============
            Text(
                text = "توضیحات",
                color = colors.textSecondary,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp)
            )

            Spacer(Modifier.height(8.dp))

            GlassTextField(
                value = description,
                onValueChange = onDescriptionChange,
                minHeight = 54.dp,
                keyboardType = KeyboardType.Text,
                hasError = descriptionError != null
            )

            // ✅ خطای فیلد توضیحات — زیر همان فیلد (مثل setError)
            if (descriptionError != null) {
                Text(
                    text = "⚠️ $descriptionError",
                    color = colors.delete,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, start = 8.dp)
                )
            }

            // ============ پیام خطا (داخل دیالوگ) ============
            if (errorMessage != null) {
                Text(
                    text = "⚠️ $errorMessage",
                    color = colors.delete,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(14.dp))
            }

            Spacer(Modifier.height(26.dp))

            // ============ دکمه‌ها ============
            if (isEditMode) {

                // حالت ویرایش: حذف + ذخیره
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Delete3DButton(
                        text = "حذف",
                        modifier = Modifier.weight(1f),
                        onClick = onDelete
                    )

                    ThreeDButton(
                        text = "ذخیره",
                        modifier = Modifier.weight(1f),
                        onClick = onConfirm
                    )
                }

            } else {

                // حالت افزودن: انصراف + افزودن
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
                        text = "افزودن",
                        modifier = Modifier.weight(1f),
                        onClick = onConfirm
                    )
                }
            }
        }
    }
}

// ============ Preview ها ============
@Preview(
    showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun CategoryDialogPreviewDark() {
    MadahiTheme(darkTheme = true) {
        Box() {

            CategoryDialogContent(
                title = "String",
                description = "String",
                isEditMode = false,
                onTitleChange = {},
                onDescriptionChange = {},
                onDismiss = {},
                onConfirm = {},
                onDelete = {},
            )
        }
    }
}

//@Preview(
//    name = "Category Dialog - Light",
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_NO
//)
//@Composable
//fun CategoryDialogPreviewLight() {
//    MadahiTheme(darkTheme = false) {
//        CategoryDialogContent(
//            title = "عنوان نمونه",
//            description = "توضیحات نمونه",
//            isEditMode = true,
//            onTitleChange = {},
//            onDescriptionChange = {},
//            onConfirm = {},
//            onDismiss = {})
//    }
//}