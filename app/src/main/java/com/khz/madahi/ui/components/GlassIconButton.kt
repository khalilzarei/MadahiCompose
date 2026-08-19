package com.khz.madahi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.border
import com.khz.madahi.ui.theme.surfaceGlass
import com.khz.madahi.ui.theme.surfaceGlassLight
import com.khz.madahi.ui.theme.textPrimary

@Composable
fun GlassIconButton(
    text: String,
    onClick: () -> Unit
) {
    val colors = LocalMadahiColors.current

    Box(
        modifier = Modifier
            .size(52.dp)
            .shadow(
                8.dp,
                CircleShape
            )
            .background(
                Brush.verticalGradient(
                    listOf(
                        colors.surfaceGlassLight,
                        colors.surfaceGlass
                    )
                ),
                CircleShape
            )
            .border(
                1.dp,
                colors.border,
                CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            color = colors.textPrimary,
            fontSize = 22.sp
        )
    }
}

@Preview(
    name = "Dialog Preview",
    showBackground = false,
)
@Composable
fun GlassIconButtonPreview() {

    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = true) {

        GlassIconButton(
            text = "",
            onClick = {},
        )
    }
}