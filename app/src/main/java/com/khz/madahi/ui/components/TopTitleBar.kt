package com.khz.madahi.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.textPrimary

@Composable
fun TopTitleBar(
    title: String,
    subTitle: String = "",
    onBack: () -> Unit,
    isCategory: Boolean = false
) {
    val colors = LocalMadahiColors.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .height(90.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isCategory) {
            GlassIconButton(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                onClick = onBack,
                tint = colors.gold
            )
        }



        Column(Modifier.padding(horizontal = 16.dp)) {

            Text(
                text = title,
                color = colors.textPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            if (subTitle.isNotEmpty()) {
                Text(
                    text = subTitle,
                    color = colors.gold,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
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
fun TopTitleBarPreview() {

    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = true) {

        TopTitleBar(
            title = "Title",
            subTitle = "",
            onBack = {},
        )
    }
}