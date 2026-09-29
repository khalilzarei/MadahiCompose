// ui/content/ContentItem.kt
package com.khz.madahi.ui.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.models.Content
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiTheme
import com.khz.madahi.ui.theme.primaryLight
import com.khz.madahi.ui.theme.textPrimary

@Composable
fun ContentItem(
    content: Content,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {

    val colors = LocalMadahiColors.current
    GlassCard3D {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .height(80.dp)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // ✅ عنوان و توضیحات
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = content.subject,
                    style = MaterialTheme.typography.titleSmall,
                    color = colors.textPrimary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (content.answer.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = content.answer,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.primaryLight,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            IconButton(
                onClick = onClick, // با کلیک روی سه نقطه مستقیم CategoryDialog باز می‌شود
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "ویرایش و گزینه‌ها",
                    tint = Color(0xFFFFC107),
                )
            }
        }

        // ============ سمت چپ: دکمه‌ها ============
//                if (content.userId != "0") {
//                    Row(
//                        horizontalArrangement = Arrangement.spacedBy(20.dp),
//                        modifier = Modifier.padding(16.dp)
//                    ) {
//                        // ✅ دکمه ویرایش
//                        IconButton(
//                            onClick = onEditClick,
//                            modifier = Modifier
//                                .size(50.dp)
//                                .clip(CircleShape)
//
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Edit,
//                                contentDescription = "ویرایش",
//                                modifier = Modifier.size(30.dp),
//                            )
//                        }
//
//                        // ✅ دکمه حذف
//                        IconButton(
//                            onClick = onDeleteClick,
//                            modifier = Modifier
//                                .size(50.dp)
//                                .clip(CircleShape)
//                                .background(
//                                    Brush.radialGradient(
//                                        colors = listOf(
//                                            Color.Red.copy(alpha = 0.15f),
//                                            Color.Transparent
//                                        )
//                                    )
//                                )
//                                .border(
//                                    width = 1.dp,
//                                    color = Color.Red.copy(alpha = 0.3f),
//                                    shape = CircleShape
//                                )
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Delete,
//                                contentDescription = "حذف",
//                                modifier = Modifier.size(30.dp),
//                                tint = MaterialTheme.colorScheme.error
//                            )
//                        }
//                    }
//                }
    }
}

// ============ Preview ============
@Preview(showBackground = false)
@Composable
fun ContentItemNohehPreview() {
    MadahiTheme(darkTheme = false) {
        ContentItem(
            content = Content(
                idContent = 0,
                id = 1,
                categoryId = 1,
                userId = 1,
                answer = "مجموعه نوحه‌های مناسبتی",
                content = "content",
                subject = "نوحه‌های محرم",
                contentType = "0"  // نوحه
            ),
            onClick = {},
            onEditClick = {},
            onDeleteClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun ContentItemRoozehPreview() {
    MadahiTheme(darkTheme = true) {
        ContentItem(
            content = Content(
                idContent = 0,
                id = 2,
                categoryId = 1,
                userId = 1,
                answer = "مجموعه روضه‌های مناسبتی",
                content = "content",
                subject = "روضه‌های محرم",
                contentType = "1"  // روضه
            ),
            onClick = {},
            onEditClick = {},
            onDeleteClick = {})
    }
}