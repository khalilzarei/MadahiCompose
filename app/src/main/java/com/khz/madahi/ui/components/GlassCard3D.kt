package com.khz.madahi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.primaryDark
import com.khz.madahi.ui.theme.primaryLight
import com.khz.madahi.ui.theme.shadow
import com.khz.madahi.ui.theme.surfaceGlass
import com.khz.madahi.ui.theme.surfaceGlassLight
import com.khz.madahi.ui.theme.textPrimary

@Composable
fun GlassCard3D(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),   // ✅ قابل تنظیم (برای Mini3DButton)
    content: @Composable () -> Unit
) {
    val colors = LocalMadahiColors.current

    val surfaceBrush = remember(colors) {
        Brush.verticalGradient(
            listOf(
                colors.surfaceGlassLight.copy(alpha = 0.98f),
                colors.surfaceGlass.copy(alpha = 0.96f),
                colors.surfaceGlass.copy(alpha = 0.92f),
                colors.primaryDark.copy(alpha = 0.72f)
            )
        )
    }

    val borderBrush = remember(colors) {
        Brush.verticalGradient(
            listOf(
                Color.White.copy(alpha = 0.32f),
                colors.primaryLight.copy(alpha = 0.22f),
                colors.gold.copy(alpha = 0.18f),
                Color.Transparent
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            // ✅ سایه از تم می‌آید (هم‌راستا با GlassCard) — مرحله ۳
            // clip در shadow پیش‌فرض true است (elevation > 0) پس highlight گوشه‌ها را نمی‌زند
            .shadow(
                elevation = 16.dp,
                shape = shape,
                ambientColor = colors.shadow,
                spotColor = colors.shadow
            )
            .background(
                surfaceBrush,
                shape
            )
//            .topHighlight(alpha = 0.08f)   // ✅ باگ رفع شد: قبلاً Box با ارتفاع صفر بود
            .border(
                1.dp,
                borderBrush,
                shape
            )
    ) {
        content()
    }
}

@Preview(showBackground = false)
@Composable
private fun GlassCard3DPreview() {
    MadahiThemeGreen(darkTheme = false) {
        val colors = LocalMadahiColors.current      // ✅ داخل تم، نه بیرون
        GlassCard3D {
            Text(
                "GlassCard",
                Modifier.padding(20.dp),
                color = colors.textPrimary
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun GlassCard3DPreviewDark() {
    MadahiThemeGreen(darkTheme = true) {
        val colors = LocalMadahiColors.current
        GlassCard3D {
            Text(
                "GlassCard",
                Modifier.padding(20.dp),
                color = colors.textPrimary
            )
        }
    }
}