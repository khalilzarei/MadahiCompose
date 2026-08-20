package com.khz.madahi.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.goldLight
import com.khz.madahi.ui.theme.textPrimary

@Composable
fun Gold3DButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val c = LocalMadahiColors.current

    Base3DButton(
        palette = Button3DPalette(
            edge = c.gold.copy(alpha = 0.45f),
            surface = listOf(
                c.goldLight,
                c.gold,
                c.gold.copy(alpha = 0.78f)
            ),
            spot = c.gold.copy(alpha = 0.45f)
        ),
        modifier = modifier,
        enabled = enabled,
        restElevation = 14.dp,      // ظاهر اصلی طلایی
        highlightAlpha = 0.25f,     // انعکاس نور روشن‌تر
        onClick = onClick
    ) {
        Text(
            text = text,
            color = c.textPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun Gold3DButtonPreview() {
    MadahiThemeGreen(darkTheme = false) {
        Gold3DButton(text = "Gold3DButton", onClick = {})
    }
}

@Preview(showBackground = false)
@Composable
private fun Gold3DButtonPreviewDark() {
    MadahiThemeGreen(darkTheme = true) {
        Gold3DButton(text = "Gold3DButton", onClick = {})
    }
}