package com.khz.madahi.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.textPrimary

@Composable
fun PoemDetailScreen(
    title: String,
    text: String,
    dialogs: List<String>,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onAddDialog: () -> Unit
) {
    val colors = LocalMadahiColors.current

    MadahiBackground {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {

            TopTitleBar(
                title = title,
                onBack = onBack
            )

            Spacer(Modifier.height(18.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                item {

                    GlassCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Column(
                            modifier = Modifier.padding(28.dp)
                        ) {

                            Text(
                                text = text,
                                color = colors.textPrimary,
                                fontSize = 20.sp,
                                lineHeight = 38.sp
                            )
                        }
                    }
                }

                item {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "دیالوگ‌ها",
                            color = colors.textPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        ThreeDButton(
                            text = "+ دیالوگ",
                            modifier = Modifier.width(135.dp),
                            onClick = onAddDialog
                        )
                    }
                }

                items(dialogs) { dialog ->

                    DialogItem(
                        text = dialog
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                ThreeDButton(
                    text = "ویرایش",
                    modifier = Modifier.weight(1f),
                    onClick = onEdit
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

@Preview(
    name = "Dialog Preview",
    showBackground = false,
)
@Composable
fun PoemDetailScreenPreview() {

    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = true) {

        PoemDetailScreen(
            title = "title",
            text = "text",
            dialogs = arrayListOf(
                "One",
                "Two",
                "Tree"
            ),
            onBack = {},
            onEdit = {},
            onDelete = {},
            onAddDialog = {},
        )

    }
}