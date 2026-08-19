package com.khz.madahi.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.primaryLight
import com.khz.madahi.ui.theme.textPrimary

@Composable
fun CategoryItem(
    title: String,
    onClick: () -> Unit
) {
    val colors = LocalMadahiColors.current

    GlassCard3D(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable { onClick() }) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = title,
                color = colors.textPrimary,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "‹",
                color = colors.primaryLight,
                fontSize = 28.sp
            )
        }
    }
}

@Preview(
    name = "Dialog Preview",
    showBackground = false,
)
@Composable
fun CategoryItemPreview() {
    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = false) {
        CategoryItem(
            title = "title",
            onClick = {},
        )

    }
}

@Preview(
    name = "Dialog Preview",
    showBackground = false,
)
@Composable
fun CategoryItemPreviewDark() {
    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = true) {

        CategoryItem(
            title = "title",
            onClick = {},
        )

    }
}