package com.khz.madahi.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.textPrimary

@Composable
fun DialogItem(
    text: String
) {
    val colors = LocalMadahiColors.current

    GlassCard(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(22.dp)
        ) {

            Text(
                text = "دیالوگ",
                color = colors.gold,
                fontSize = 13.sp
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = text,
                color = colors.textPrimary,
                fontSize = 17.sp,
                lineHeight = 30.sp
            )
        }
    }
}

@Preview(
    name = "Dialog Preview",
    showBackground = false,
)
@Composable
fun DialogItemPreview() {

    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = true) {
        // ✅ مستقیماً CustomDialog را صدا بزن
        DialogItem(
            text = "DialogItem",
        )
    }
}