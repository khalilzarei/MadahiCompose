package com.khz.madahi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.border
import com.khz.madahi.ui.theme.borderLight
import com.khz.madahi.ui.theme.shadow
import com.khz.madahi.ui.theme.surfaceGlass
import com.khz.madahi.ui.theme.surfaceGlassLight
import com.khz.madahi.ui.theme.textPrimary

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val colors = LocalMadahiColors.current

    Box(
        modifier = modifier
            .shadow(
                elevation = 14.dp,
                shape = RoundedCornerShape(22.dp),
                ambientColor = colors.shadow,
                spotColor = colors.shadow
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colors.surfaceGlassLight.copy(alpha = 0.78f),
                        colors.surfaceGlass.copy(alpha = 0.55f)
                    )
                ),
                shape = RoundedCornerShape(22.dp)
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colors.borderLight,
                        colors.border.copy(alpha = 0.35f)
                    )
                ),
                shape = RoundedCornerShape(22.dp)
            )
    ) {
        content()
    }
}

@Preview(
    name = "Dialog Preview",
    showBackground = false,
)
@Composable
fun GlassCardPreview() {

    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = true) {
        // ✅ مستقیماً CustomDialog را صدا بزن
        GlassCard() {
            Text(
                "GlassCard",
                modifier = Modifier.padding(10.dp),
                color = colors.textPrimary
            )
        }
    }
}