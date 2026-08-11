// ui/content/DeleteContentDialog.kt
package com.khz.madahi.ui.content

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.khz.madahi.models.Content

@Composable
fun DeleteContentDialog(
    content: Content,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "حذف محتوا",
                color = MaterialTheme.colorScheme.error
            )
        },
        text = {
            Text(
                text = "آیا از حذف «${content.subject}» اطمینان دارید؟",
                style = MaterialTheme.typography.bodyLarge
            )
        },
        confirmButton = {
            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("حذف")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("انصراف")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Preview(showBackground = true)
@Composable
fun DeleteContentDialogPreview() {
    MaterialTheme {
        DeleteContentDialog(
            content = Content(
                idContent = 0,
                id = "1",
                categoryId = "1",
                userId = "1",
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