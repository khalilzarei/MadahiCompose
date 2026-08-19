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
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary

@Composable
fun PoemsScreen(
    category: String,
    poems: List<String>,
    onPoemClick: (String) -> Unit,
    onAddPoem: () -> Unit,
    onBack: () -> Unit
) {
    val colors = LocalMadahiColors.current

    MadahiBackground {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {

            TopTitleBar(
                title = category,
                onBack = onBack
            )

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "اشعار",
                        color = colors.textPrimary,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "${poems.size} شعر",
                        color = colors.textMuted,
                        fontSize = 14.sp
                    )
                }

                ThreeDButton(
                    text = "+ شعر جدید",
                    modifier = Modifier.width(145.dp),
                    onClick = onAddPoem
                )
            }

            Spacer(Modifier.height(18.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(poems) { poem ->

                    PoemItem(
                        title = poem,
                        onClick = {
                            onPoemClick(poem)
                        })
                }
            }
        }
    }
}

@Preview(
    name = "Dialog Preview",
    showBackground = false,
)
@Composable
fun PoemsScreenPreview() {

    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = true) {

        PoemsScreen(
            category = "title",
            poems = arrayListOf(
                "One",
                "Two",
                "Tree"
            ),
            onPoemClick = {},
            onAddPoem = {},
            onBack = {},
        )

    }
}