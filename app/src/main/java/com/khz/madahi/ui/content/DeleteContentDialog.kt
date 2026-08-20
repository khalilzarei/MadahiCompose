// ui/content/DeleteContentDialog.kt
package com.khz.madahi.ui.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.khz.madahi.models.Content
import com.khz.madahi.ui.common.DialogFullscreenWindow
import com.khz.madahi.ui.components.Delete3DButton
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.components.ThreeDButton
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiTheme
import com.khz.madahi.ui.theme.delete
import com.khz.madahi.ui.theme.textMuted

// ============================================================
// دیالوگ تأیید حذف محتوا — طراحی شیشه‌ای و سه‌بعدی
// امضا دقیقاً مثل نسخه قبلی است
// ============================================================

@Composable
fun DeleteContentDialog(
    content: Content,
    onDelete: () -> Unit,
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
            DialogFullscreenWindow()

            DeleteContentDialogContent(
                content = content,
                onDelete = onDelete,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
fun DeleteContentDialogContent(
    content: Content,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
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
                .padding(
                    horizontal = 24.dp,
                    vertical = 28.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ============ آیکون هشدار ============
            Text(
                text = "🗑️",
                fontSize = 48.sp
            )

            Spacer(Modifier.height(14.dp))

            // ============ عنوان ============
            Text(
                text = "حذف محتوا",
                color = colors.delete,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))

            // ============ متن تأیید ============
            Text(
                text = "آیا از حذف «${content.subject}» اطمینان دارید؟",
                color = colors.textMuted,
                fontSize = 15.sp,
                lineHeight = 26.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(26.dp))

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

                Delete3DButton(
                    text = "حذف",
                    modifier = Modifier.weight(1f),
                    onClick = onDelete
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeleteContentDialogPreview() {
    MadahiTheme(darkTheme = true) {
        DeleteContentDialog(
            content = Content(
                idContent = 0,
                id = 1,
                categoryId = 1,
                userId = 1,
                answer = "مجموعه نوحه‌های مناسبتی",
                content = "<p>متن نمونه برای نمایش در صفحه جزئیات محتوا. این متن برای تست و نمایش ظاهر صفحه استفاده می‌شود.</p>",
                subject = "نوحه‌های محرم",
                contentType = "0"
            ),
            onDelete = {},
            onDismiss = {},
        )
    }
}