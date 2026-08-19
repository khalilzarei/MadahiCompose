// ui/category/CategoryItem.kt
package com.khz.madahi.ui.category

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ViewHeadline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.models.Category
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.primaryLight
import com.khz.madahi.ui.theme.textPrimary

@Composable
fun CategoryItem(
    category: Category,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
//    val colors = getDangerDialogColors()

    val colors = LocalMadahiColors.current
    val canManage = category.userId != "0"
    val shape = RoundedCornerShape(10.dp)

    // وضعیت نمایش یا عدم نمایش منوی منوی سه نقطه
    var showMenu by remember { mutableStateOf(false) }

    GlassCard3D(
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp)
            .clickable { onClick() }) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // ============ عنوان و توضیحات ============
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = colors.textPrimary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (category.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = category.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.primaryLight,
                        maxLines = 1,
                        fontSize = 12.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // ============ آیکون سه نقطه و منوی کشویی ============
            if (canManage) {
                IconButton(
                    onClick = onEditClick, // با کلیک روی سه نقطه مستقیم CategoryDialog باز می‌شود
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ViewHeadline,
                        contentDescription = "ویرایش و گزینه‌ها",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(34.dp)
                    )
                }
            }
        }
    }
}

// ============ Preview ============
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun CategoryItemPreview() {
    MadahiThemeGreen(darkTheme = true) {
        CategoryItem(
            category = Category(
                id = "1",
                userId = "1",
                title = "علمدار ",
                description = "حاج محمود کریمی"
            ),
            onClick = {},
            onEditClick = {},
            onDeleteClick = {})
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun CategoryItemDefaultPreview() {
    MadahiThemeGreen(darkTheme = false) {
        CategoryItem(
            category = Category(
                id = "1",
                userId = "1",
                title = "ای اهل حرم میرو و علمدار نیامد",
                description = "حاج محمود کریمی"
            ),
            onClick = {},
            onEditClick = {},
            onDeleteClick = {})
    }
}

//@Preview(
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_NO
//)
//@Composable
//fun CategoryItemDefaultNoActionsPreview() {
//    MadahiTheme(darkTheme = false) {
//        CategoryItem(
//            category = Category(
//                id = "2",
//                userId = "0",
//                title = "دسته پیش‌فرض",
//                description = "قابل ویرایش نیست"
//            ),
//            onClick = {},
//            onEditClick = {},
//            onDeleteClick = {})
//    }
//}