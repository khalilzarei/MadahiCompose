package com.khz.madahi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.primaryDark
import com.khz.madahi.ui.theme.primaryLight
import com.khz.madahi.ui.theme.surfaceGlass
import com.khz.madahi.ui.theme.surfaceGlassLight
import com.khz.madahi.ui.theme.textPrimary

@Composable
fun GlassCard3D(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val colors = LocalMadahiColors.current

    val shape = RoundedCornerShape(20.dp)

    Box(
        modifier = modifier
            .shadow(
                elevation = 16.dp,
                shape = shape,
                ambientColor = Color.Black.copy(alpha = 0.70f),
                spotColor = Color.Black.copy(alpha = 0.85f)
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colors.surfaceGlassLight.copy(alpha = 0.98f),
                        colors.surfaceGlass.copy(alpha = 0.96f),
                        colors.surfaceGlass.copy(alpha = 0.92f),
                        colors.primaryDark.copy(alpha = 0.72f)
                    )
                ),
                shape = shape
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.32f),
                        colors.primaryLight.copy(alpha = 0.22f),
                        colors.gold.copy(alpha = 0.18f),
                        Color.Transparent
                    )
                ),
                shape = shape
            )
    ) {

        /*
         * Highlight بالای کارت
         */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 1.dp,
                    vertical = 1.dp
                )
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.08f),
                            Color.Transparent
                        )
                    ),
                    RoundedCornerShape(23.dp)
                )
        )

        content()
    }
}

@Preview(
    name = "Dialog Preview",
    showBackground = false,
)
@Composable
fun GlassCard3DPreview() {

    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = false) {
        // ✅ مستقیماً CustomDialog را صدا بزن
        GlassCard3D() {
            Text(
                "GlassCard",
                modifier = Modifier.padding(20.dp),
                color = colors.textPrimary
            )
        }
    }
}

@Preview(
    name = "Dialog Preview",
    showBackground = false,
)
@Composable
fun GlassCard3DPreviewDark() {

    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = true) {
        // ✅ مستقیماً CustomDialog را صدا بزن
        GlassCard3D() {
            Text(
                "GlassCard",
                modifier = Modifier.padding(20.dp),
                color = colors.textPrimary
            )
        }
    }
}