// ui/category/DeleteCategoryDialog.kt
package com.khz.madahi.ui.category

import android.annotation.SuppressLint
import android.content.res.Configuration
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.graphics.drawable.toDrawable
import com.khz.madahi.R
import com.khz.madahi.models.Category
import com.khz.madahi.ui.common.DecorativeCorners
import com.khz.madahi.ui.common.RibbonButton
import com.khz.madahi.ui.theme.MadahiTheme
import com.khz.madahi.ui.theme.getDangerDialogColors

@Composable
fun DeleteCategoryDialogContent(
    category: Category,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = getDangerDialogColors()

    Column(modifier = Modifier.fillMaxWidth()) {
        // ============ Top Title (اختیاری) ============

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
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            // ============ گوشه‌های تزئینی ============
            DecorativeCorners()

            // ============ محتوای اصلی ============
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 24.dp,
                        vertical = 28.dp
                    )
            ) {

                Text(
                    text = "حذف مداحی",
                    color = colors.title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "آیا می‌خواهید دسته بندی «${category.title}» را حذف کنید؟",
                    color = colors.body,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(40.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    RibbonButton(
                        text = "انصراف",
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    )

                    RibbonButton(
                        text = "حذف",
                        onClick = onDelete,
                        modifier = Modifier.weight(1f),
                        isDanger = true
                    )
                }
            }
        }
    }
}

@SuppressLint("UseKtx")
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
                DeleteCategoryDialogContent(
                    category = category,
                    onDelete = onDelete,
                    onDismiss = onDismiss
                )
            }
        }
    }
}

//// ============ Preview ها ============
//@Preview(
//    name = "Delete Dialog - Dark",
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES
//)
//@Composable
//fun DeleteCategoryDialogPreviewDark() {
//    MadahiTheme(darkTheme = true) {
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