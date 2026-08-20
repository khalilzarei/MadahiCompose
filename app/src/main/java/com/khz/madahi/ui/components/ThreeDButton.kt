package com.khz.madahi.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.primaryDark
import com.khz.madahi.ui.theme.primaryLight
import com.khz.madahi.ui.theme.textPrimary

@Composable
fun ThreeDButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val c = LocalMadahiColors.current
    Base3DButton(
        palette = Button3DPalette(
            edge = c.primaryDark,
            surface = listOf(
                c.primaryLight,
                c.primary,
                c.primaryDark
            ),
            spot = c.primaryDark
        ),
        modifier = modifier,
        enabled = enabled,
        onClick = onClick
    ) {
        Text(
            text,
            color = c.textPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(
    name = "Dialog Preview",
    showBackground = false,
)
@Composable
fun ThreeDButtonPreview() {

    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = false) {
        // ✅ مستقیماً CustomDialog را صدا بزن
        ThreeDButton(
            text = "ThreeDButton",
            enabled = false,
            onClick = {})
    }
}

@Preview(
    name = "Dialog Preview",
    showBackground = false,
)
@Composable
fun ThreeDButtonPreviewDark() {

    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = true) {
        // ✅ مستقیماً CustomDialog را صدا بزن
        ThreeDButton(
            text = "ThreeDButton",
            enabled = false,
            onClick = {})
    }
}