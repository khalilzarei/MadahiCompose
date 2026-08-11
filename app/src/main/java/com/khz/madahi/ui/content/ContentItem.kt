// ui/content/ContentItem.kt
package com.khz.madahi.ui.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.RamenDining
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.models.Content
import com.khz.madahi.ui.theme.Gold
import com.khz.madahi.ui.theme.PrimaryGreen
import com.khz.madahi.ui.theme.PrimaryGreenDark


@Composable
fun ContentItem(
    content: Content,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    // تشخیص نوع محتوا و رنگ‌بندی
    val isNoheh = content.contentType == "0"

    // رنگ‌های اصلی بر اساس نوع
    val primaryColor = if (isNoheh) Gold else PrimaryGreen
    val primaryColorDark = if (isNoheh) Gold else PrimaryGreenDark
    val primaryColorAlpha = if (isNoheh) Gold.copy(alpha = 0.5f) else PrimaryGreen.copy(alpha = 0.3f)

    // آیکون بر اساس نوع
    val icon = if (isNoheh) {
        Icons.Default.RamenDining  // نوحه
    } else {
        Icons.Default.Mosque       // روضه
    }

    // متن نوع
    val typeText = if (isNoheh) "نوحه" else "روضه"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        // ============ بک‌گراند مذهبی ============
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            if (isNoheh) Gold.copy(alpha = 0.2f) else PrimaryGreenDark.copy(alpha = 0.6f),
                            if (isNoheh) Gold.copy(alpha = 0.08f) else PrimaryGreen.copy(alpha = 0.15f),
                            if (isNoheh) Gold.copy(alpha = 0.15f) else PrimaryGreenDark.copy(alpha = 0.3f)
                        )
                    )
                )
        ) {
            // ============ محتوای اصلی آیتم ============
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // ============ سمت راست: آیکون + عنوان ============
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onClick() }
                        .height(80.dp)
                        .padding(start = 12.dp, end = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // ✅ آیکون با هاله نور
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        primaryColor.copy(alpha = 0.25f),
                                        primaryColor.copy(alpha = 0.15f),
                                        Color.Transparent
                                    ),
                                    radius = 0.8f
                                )
                            )
                            .border(
                                width = 1.5.dp,
                                color = primaryColor.copy(alpha = 0.4f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = primaryColor,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // ✅ عنوان و توضیحات
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = content.subject,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                fontSize = 20.sp
                            )
                            // ✅ برچسب نوع محتوا
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = primaryColor.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = typeText,
                                    fontSize = 10.sp,
                                    color = primaryColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        if (content.answer.isNotEmpty()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "ﷺ",
                                    fontSize = 14.sp,
                                    color = primaryColor.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = content.answer,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }

                // ============ سمت چپ: دکمه‌ها ============
                if (content.userId != "0") {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // ✅ دکمه ویرایش
                        IconButton(
                            onClick = onEditClick,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            primaryColor.copy(alpha = 0.2f),
                                            Color.Transparent
                                        )
                                    )
                                )
                                .border(
                                    width = 1.dp,
                                    color = primaryColor.copy(alpha = 0.3f),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "ویرایش",
                                modifier = Modifier.size(30.dp),
                                tint = primaryColor
                            )
                        }

                        // ✅ دکمه حذف
                        IconButton(
                            onClick = onDeleteClick,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            Color.Red.copy(alpha = 0.15f),
                                            Color.Transparent
                                        )
                                    )
                                )
                                .border(
                                    width = 1.dp,
                                    color = Color.Red.copy(alpha = 0.3f),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "حذف",
                                modifier = Modifier.size(30.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }

            // ============ خط تزئینی پایین ============
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .height(2.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                primaryColor.copy(alpha = 0.5f),
                                primaryColor.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // ============ گوشه‌های تزئینی ============
            // گوشه بالا-راست
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.TopEnd)
                    .padding(top = 4.dp, end = 4.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                primaryColor.copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )

            // گوشه پایین-چپ
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.BottomStart)
                    .padding(bottom = 4.dp, start = 4.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                primaryColor.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )
        }
    }
}

// ============ Preview ============
@Preview(showBackground = true)
@Composable
fun ContentItemNohehPreview() {
    MaterialTheme {
        ContentItem(
            content = Content(
                idContent = 0,
                id = "1",
                categoryId = "1",
                userId = "1",
                answer = "مجموعه نوحه‌های مناسبتی",
                content = "content",
                subject = "نوحه‌های محرم",
                contentType = "0"  // نوحه
            ),
            onClick = {},
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ContentItemRoozehPreview() {
    MaterialTheme {
        ContentItem(
            content = Content(
                idContent = 0,
                id = "2",
                categoryId = "1",
                userId = "1",
                answer = "مجموعه روضه‌های مناسبتی",
                content = "content",
                subject = "روضه‌های محرم",
                contentType = "1"  // روضه
            ),
            onClick = {},
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}