// ui/category/CategoryItem.kt
package com.khz.madahi.ui.category

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.R
import com.khz.madahi.models.Category
import com.khz.madahi.ui.theme.MadahiTheme
import com.khz.madahi.ui.theme.PrimaryGreenDark
import com.khz.madahi.ui.theme.getDangerDialogColors

// ============ گوشه‌های تزئینی (استفاده مجدد از corner.png موجود در پروژه) ============
@Composable
private fun BoxScope.DecorativeCorners(size: Int = 40) {
    val corner = painterResource(R.drawable.corner)

    Image(
        painter = corner,
        contentDescription = null,
        modifier = Modifier
            .align(Alignment.TopStart)
            .size(size.dp)
            .rotate(-90f),
        contentScale = ContentScale.FillBounds
    )
    Image(
        painter = corner,
        contentDescription = null,
        modifier = Modifier
            .align(Alignment.TopEnd)
            .size(size.dp)
            .rotate(180f),
        contentScale = ContentScale.FillBounds
    )
    Image(
        painter = corner,
        contentDescription = null,
        modifier = Modifier
            .align(Alignment.BottomStart)
            .size(size.dp),
        contentScale = ContentScale.FillBounds
    )
    Image(
        painter = corner,
        contentDescription = null,
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .size(size.dp)
            .rotate(90f),
        contentScale = ContentScale.FillBounds
    )
}

// ============ Category Item (نسخه جدید مطابق طرح ارسالی) ============
@Composable
fun CategoryItem(
    category: Category,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val colors = getDangerDialogColors()
    val canManage = category.userId != "0"

    val corner = painterResource(R.drawable.ic_kenar)
    val size = 80.dp
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(
                vertical = 5.dp,
                horizontal = 20.dp
            )
            .height(80.dp)
    ) {
        Image(
            painter = corner,
            contentDescription = null,
            modifier = Modifier
                .width(size / 2)
                .height(size)
                .rotate(180f),
            contentScale = ContentScale.FillBounds
        )
        Box(
            modifier = Modifier
                .height(size)
                .weight(1f)
                .background(
                    shape = RoundedCornerShape(10.dp),
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF07130c).copy(0.9f),
                            PrimaryGreenDark.copy(0.5f),
                            Color(0xFF07130c).copy(0.5f),
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    color = Color(0xFFFFC107),
                    shape = RoundedCornerShape(10.dp) // shape را به border بدهید
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .height(80.dp)
                    .padding(5.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onClick() }) {
                    // ✅ گوشه‌های تزئینی طلایی

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        // ============ عنوان و توضیحات — وسط ============
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 12.dp),
                        ) {
                            Text(
                                text = category.title,
                                style = MaterialTheme.typography.titleLarge,
                                color = colors.title,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                            )
                            if (category.description.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = category.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colors.body,
                                    maxLines = 1,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // ============ دکمه‌های ویرایش/حذف — سمت چپ تصویر ============
                        if (canManage) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = onEditClick,
                                    modifier = Modifier.size(46.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ModeEdit,
                                        contentDescription = "ویرایش",
                                        tint = colors.border,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                                IconButton(
                                    onClick = onDeleteClick,
                                    modifier = Modifier.size(46.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "حذف",
                                        tint = colors.deleteBackground,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }
                        } else {
                            // فضای خالی هم‌اندازه برای حفظ تقارن وقتی دکمه‌ها نمایش داده نمی‌شوند
                            Spacer(modifier = Modifier.width(40.dp))
                        }
                    }
                }
            }
        }
        Image(
            painter = corner,
            contentDescription = null,
            modifier = Modifier
                .width(size / 2)
                .height(size),
            contentScale = ContentScale.FillBounds
        )
    }

}

// ============ Preview ============
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun CategoryItemPreview() {
    MadahiTheme(darkTheme = true) {
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
    MadahiTheme(darkTheme = false) {
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