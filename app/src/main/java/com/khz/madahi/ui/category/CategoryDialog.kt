// ui/category/CategoryDialog.kt
package com.khz.madahi.ui.category

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.graphics.drawable.toDrawable
import com.khz.madahi.R
import com.khz.madahi.ui.common.DecorativeCorners
import com.khz.madahi.ui.common.RibbonButton
import com.khz.madahi.ui.theme.getDangerDialogColors

@Composable
fun CategoryDialogContent(
    title: String,
    description: String,
    isEditMode: Boolean,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    onDelete: () -> Unit // ✅ اضافه شدن اکشن حذف
) {
    val colors = getDangerDialogColors()

    Column(modifier = Modifier.fillMaxWidth()) {
        // ============ Top Title ============
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 30.dp,
                    end = 30.dp
                )
        ) {
            Image(
                painter = painterResource(R.drawable.top_title),
                contentDescription = "Top Title",
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.FillBounds
            )
        }

        // ============ کارت اصلی ============
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    color = colors.background,
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    width = 2.dp,
                    color = colors.border,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            // ============ گوشه‌های تزئینی ============
            DecorativeCorners()

            // ============ محتوای اصلی ============
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 24.dp
                    )
            ) {
                Text(
                    text = if (isEditMode) "✏️ ویرایش دسته‌بندی" else "📁 دسته‌بندی جدید",
                    color = colors.title,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = onTitleChange,
                    label = { Text("عنوان") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    label = { Text("توضیحات") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(40.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // دکمه سمت چپ: در حالت ویرایش «حذف» و در حالت ساخت جدید «انصراف»
                    RibbonButton(
                        text = if (isEditMode) "حذف" else "انصراف",
                        onClick = if (isEditMode) onDelete else onDismiss,
                        modifier = Modifier.weight(1f),
                        isDanger = true
                    )
                    // دکمه سمت راست: در حالت ویرایش «ذخیره» و در حالت ساخت جدید «افزودن»
                    RibbonButton(
                        text = if (isEditMode) "ذخیره" else "افزودن",
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@SuppressLint("UseKtx")
@Composable
fun CategoryDialog(
    title: String,
    description: String,
    isEditMode: Boolean,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onDelete: () -> Unit // ✅ اضافه شدن اکشن حذف
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            val view = LocalView.current
            SideEffect {
                val window = (view.parent as? DialogWindowProvider)?.window
                window?.setBackgroundDrawable(
                    android.graphics.Color.TRANSPARENT.toDrawable()
                )
            }

            Surface(
                color = androidx.compose.ui.graphics.Color.Transparent,
                tonalElevation = 0.dp,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .wrapContentHeight()
            ) {
                CategoryDialogContent(
                    title = title,
                    description = description,
                    isEditMode = isEditMode,
                    onTitleChange = onTitleChange,
                    onDescriptionChange = onDescriptionChange,
                    onConfirm = onConfirm,
                    onDismiss = onDismiss,
                    onDelete = onDelete // ✅ پاس دادن اکشن حذف به محتوای دیالوگ
                )
            }
        }
    }
}

// ============ Preview ها ============
//@Preview(
//    name = "Category Dialog - Dark",
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES
//)
//@Composable
//fun CategoryDialogPreviewDark() {
//    MadahiTheme(darkTheme = true) {
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