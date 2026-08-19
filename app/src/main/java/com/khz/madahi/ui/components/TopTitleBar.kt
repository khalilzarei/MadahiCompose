package com.khz.madahi.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
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
fun TopTitleBar(
    title: String,
    onBack: () -> Unit
) {
    val colors = LocalMadahiColors.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        GlassIconButton(
            text = "‹",
            onClick = onBack
        )

        Spacer(Modifier.width(16.dp))

        Text(
            text = title,
            color = colors.textPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
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
            title = "",
            onBack = {},
        )
    }
}