package com.khz.madahi.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.textSecondary

@Composable
fun EditPoemScreen(
    title: String,
    text: String,
    onTitleChange: (String) -> Unit,
    onTextChange: (String) -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit
) {
    val colors = LocalMadahiColors.current

    MadahiBackground {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(26.dp)
        ) {

            TopTitleBar(
                title = "ویرایش شعر",
                onBack = onBack
            )

            Spacer(Modifier.height(20.dp))

            GlassCard(
                modifier = Modifier.weight(1f)
            ) {

                Column(
                    modifier = Modifier.padding(24.dp)
                ) {

                    Text(
                        text = "عنوان شعر",
                        color = colors.textSecondary
                    )

                    Spacer(Modifier.height(8.dp))

                    GlassTextField(
                        value = title,
                        onValueChange = onTitleChange,
                        keyboardType = KeyboardType.Text
                    )

                    Spacer(Modifier.height(18.dp))

                    Text(
                        text = "متن شعر",
                        color = colors.textSecondary
                    )

                    Spacer(Modifier.height(8.dp))

                    GlassTextField(
                        value = text,
                        onValueChange = onTextChange,
                        minHeight = 300.dp
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            Row(modifier = Modifier.fillMaxWidth()) {

                ThreeDButton(
                    text = "ذخیره تغییرات",
                    modifier = Modifier.weight(1f),
                    onClick = onSave
                )

                Spacer(Modifier.width(12.dp))

                Delete3DButton(
                    text = "حذف شعر",
                    modifier = Modifier.weight(1f),
                    onClick = onDelete
                )

            }
        }
    }
}

@Preview(
    name = "Dialog Preview",
    showBackground = false,
)
@Composable
fun EditPoemScreenPreview() {

    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = true) {
        // ✅ مستقیماً CustomDialog را صدا بزن
        EditPoemScreen(
            title = "title",
            text = "text",
            onTitleChange = {},
            onTextChange = {},
            onSave = {},
            onDelete = {},
            onBack = {},
        )
    }
}