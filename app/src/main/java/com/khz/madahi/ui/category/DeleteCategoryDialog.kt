// ui/category/DeleteCategoryDialog.kt
package com.khz.madahi.ui.category

import android.content.res.Configuration
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
import com.khz.madahi.models.Category
import com.khz.madahi.ui.common.DialogFullscreenWindow
import com.khz.madahi.ui.components.Delete3DButton
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.components.ThreeDButton
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiTheme
import com.khz.madahi.ui.theme.delete
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary

// ============================================================
// دیالوگ تأیید حذف دسته‌بندی — طراحی شیشه‌ای و سه‌بعدی
// امضا دقیقاً مثل نسخه قبلی است
// ============================================================

@Composable
fun DeleteCategoryDialog(
    category: Category,
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

            DeleteCategoryDialogContent(
                category = category,
                onDelete = onDelete,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
fun DeleteCategoryDialogContent(
    category: Category,
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
                .padding(horizontal = 24.dp, vertical = 28.dp),
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
                text = "حذف دسته‌بندی",
                color = colors.delete,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))

            // ============ متن تأیید ============
            Text(
                text = "آیا می‌خواهید دسته‌بندی «${category.title}» را حذف کنید؟\nهمه محتواهای آن نیز حذف می‌شود.",
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


//// ============ Preview ها ============
@Preview(
    name = "Delete Dialog - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun DeleteCategoryDialogPreviewDark() {
    MadahiTheme(darkTheme = true) {
        DeleteCategoryDialogContent(
            category = Category(
                id = 2,
                userId = 1,
                title = "نوحه محرم",
                description = ""
            ),
            onDelete = {},
            onDismiss = {})
    }
}
//
//@Preview(
//    name = "Delete Dialog - Light",
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_NO
//)
//@Composable
//fun DeleteCategoryDialogPreviewLight() {
//    MadahiTheme(darkTheme = false) {
//        DeleteCategoryDialogContent(
//            category = Category(
//                id = "2",
//                userId = "1",
//                title = "نوحه محرم",
//                description = ""
//            ),
//            onDelete = {},
//            onDismiss = {})
//    }
//}