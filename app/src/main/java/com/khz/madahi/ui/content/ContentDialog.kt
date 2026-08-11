// ui/content/ContentDialog.kt
package com.khz.madahi.ui.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khz.madahi.ui.theme.Gold
import com.khz.madahi.ui.theme.PrimaryGreen

@Composable
fun ContentDialog(
    subject: String,
    answer: String,
    contentText: String,
    isNoheh: Boolean,
    isEditMode: Boolean,
    onSubjectChange: (String) -> Unit,
    onAnswerChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onContentTypeChange: (Boolean) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isEditMode) "✏️ ویرایش محتوا" else "📝 محتوای جدید",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // ✅ انتخاب نوع محتوا
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = isNoheh,
                        onClick = { onContentTypeChange(true) },
                        label = { Text("🕌 نوحه") },
                        modifier = Modifier.weight(1f),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Gold.copy(alpha = 0.2f),
                            selectedLabelColor = Gold
                        )
                    )
                    FilterChip(
                        selected = !isNoheh,
                        onClick = { onContentTypeChange(false) },
                        label = { Text("🕋 روضه") },
                        modifier = Modifier.weight(1f),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryGreen.copy(alpha = 0.2f),
                            selectedLabelColor = PrimaryGreen
                        )
                    )
                }

                // ✅ عنوان
                OutlinedTextField(
                    value = subject,
                    onValueChange = onSubjectChange,
                    label = { Text("عنوان") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                // ✅ جواب
                OutlinedTextField(
                    value = answer,
                    onValueChange = onAnswerChange,
                    label = { Text("جواب (اختیاری)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                // ✅ متن اصلی
                OutlinedTextField(
                    value = contentText,
                    onValueChange = onContentChange,
                    label = { Text("متن") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (isEditMode) "💾 ویرایش" else "➕ افزودن")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("❌ انصراف")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        shape = RoundedCornerShape(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun ContentDialogPreview() {
    MaterialTheme {
        ContentDialog(
            subject = "subject",
            answer = "answer",
            contentText = "<p>متن نمونه برای نمایش در صفحه جزئیات محتوا. این متن برای تست و نمایش ظاهر صفحه استفاده می‌شود.</p>",
            isNoheh = false,
            isEditMode = true,
            onSubjectChange = {},
            onAnswerChange = {},
            onContentChange = {},
            onContentTypeChange = {},
            onDismiss = {},
            onConfirm = {},
        )
    }
}