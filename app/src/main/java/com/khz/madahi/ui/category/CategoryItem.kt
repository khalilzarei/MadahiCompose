// ui/category/CategoryItem.kt
package com.khz.madahi.ui.category

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.R
import com.khz.madahi.models.Category
import com.khz.madahi.ui.theme.MadahiTheme
import com.khz.madahi.ui.theme.getDangerDialogColors

// ============ گوشه‌های تزئینی (استفاده مجدد از corner.png موجود در پروژه) ============
@Composable
private fun BoxScope.DecorativeCorners(size: Int = 34) {
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


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 5.dp,
                bottom = 5.dp
            )
            .height(92.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.background
        ),
        border = BorderStroke(
            1.5.dp,
            colors.border
        ),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick() }) {
            // ✅ گوشه‌های تزئینی طلایی
            DecorativeCorners()

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // ============ نشان دایره‌ای کتاب — سمت راست تصویر ============
                Box(
                    modifier = Modifier.size(42.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // نقطه‌های تزئینی در ۴ جهت
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(colors.border)
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(colors.border)
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(colors.border)
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(colors.border)
                    )


                    // نشان اصلی
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(colors.cancelBackground)
                            .border(
                                width = 2.dp,
                                color = colors.border,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(
                                R.drawable.ic_book // ✅ تصویر دارک
                            ),
                            contentDescription = "Splash Background",
                            modifier = Modifier.size(25.dp),
                            contentScale = ContentScale.FillBounds

                        )
                    }
                }

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
                        Spacer(modifier = Modifier.height(8.dp))
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