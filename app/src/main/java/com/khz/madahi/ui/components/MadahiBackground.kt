package com.khz.madahi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiTheme
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.primaryDark
import com.khz.madahi.ui.theme.primaryLight
import com.khz.madahi.ui.theme.textPrimary

@Composable
fun MadahiBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val colors = LocalMadahiColors.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        colors.background,
                        colors.surface,
                        colors.background
                    )
                )
            )
    ) {

        // =====================================================
        // Emerald light source
        // =====================================================

        Box(
            modifier = Modifier
                .size(500.dp)
                .blur(120.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            colors.primaryLight.copy(alpha = 0.10f),
                            colors.primary.copy(alpha = 0.035f),
                            Color.Transparent
                        )
                    )
                )
        )

        // =====================================================
        // Golden light
        // =====================================================

        Box(
            modifier = Modifier
                .size(320.dp)
                .blur(120.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            colors.gold.copy(alpha = 0.055f),
                            Color.Transparent
                        )
                    )
                )
        )

        // =====================================================
        // Subtle depth
        // =====================================================

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.025f),
                            Color.Transparent,
                            colors.primaryDark.copy(alpha = 0.045f)
                        )
                    )
                )
        )

        // =====================================================
        // Center light
        // =====================================================

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        radius = 1000f,
                        colors = listOf(
                            Color.White.copy(alpha = 0.035f),
                            Color.Transparent
                        )
                    )
                )
        )

        // =====================================================
        // Soft vignette
        // =====================================================

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        radius = 1300f,
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.10f)
                        )
                    )
                )
        )

        content()
    }
}

@Preview(
    name = "Dialog Preview",
    showBackground = true,
)
@Composable
fun MadahiBackgroundPreviewDark() {

    val colors = LocalMadahiColors.current
    MadahiTheme(darkTheme = true) {
        MadahiBackground() {
            Text(
                "MadahiBackground",
                color = colors.textPrimary
            )
        }
    }
}


@Preview(
    name = "Dialog Preview",
    showBackground = true,
)
@Composable
fun MadahiBackgroundPreview() {

    val colors = LocalMadahiColors.current
    MadahiTheme(darkTheme = false) {
        MadahiBackground() {
            Text(
                "MadahiBackground",
                color = colors.textPrimary
            )
        }
    }
}
