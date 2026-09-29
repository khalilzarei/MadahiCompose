package com.khz.madahi.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.primaryDark
import com.khz.madahi.ui.theme.primaryLight

// ============================================================
// پالت رنگی دکمه سه‌بعدی
// ============================================================

data class Button3DPalette(
    val edge: Color,           // لایه ضخامت (تیره‌ترین)
    val surface: List<Color>,  // گرادیان بدنه
    val spot: Color            // رنگ spotColor سایه
)

internal val Btn3DShape = RoundedCornerShape(18.dp)

private val ButtonSurfaceHeight = 45.dp
private val ButtonDepth = 6.dp

// ============================================================
// Highlight شیشه‌ای بالای سطح
//
// با drawWithCache: Brush فقط وقتی size عوض شود دوباره ساخته می‌شود.
// با onDrawBehind: زیر محتوا کشیده می‌شود، نه رویش.
// ============================================================

fun Modifier.topHighlight(
    fraction: Float = 0.45f,
    alpha: Float = 0.16f
): Modifier = this.drawWithCache {
    val highlightHeight = size.height * fraction
    val brush = Brush.verticalGradient(
        colors = listOf(
            Color.White.copy(alpha = alpha),
            Color.Transparent
        ),
        startY = 0f,
        endY = highlightHeight
    )
    onDrawBehind {
        drawRect(
            brush = brush,
            size = Size(
                size.width,
                highlightHeight
            )
        )
    }
}

// ============================================================
// دکمه پایه سه‌بعدی
// ============================================================

@Composable
fun Base3DButton(
    palette: Button3DPalette,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = Btn3DShape,
    restElevation: Dp = 12.dp,
    highlightAlpha: Float = 0.16f,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val isPressed by interaction.collectIsPressedAsState()

    /*
     * ⚠️ عمداً `by` استفاده نشده.
     * State نگه داشته می‌شود تا خواندنش داخل graphicsLayer
     * بیفتد → فقط فاز draw، بدون recomposition در هر فریم.
     */
    val pressTranslation = animateDpAsState(
        targetValue = if (isPressed) 4.dp else 0.dp,
        label = "press_translation"
    )
    val pressElevation = animateDpAsState(
        targetValue = if (isPressed) 4.dp else restElevation,
        label = "press_elevation"
    )

    // تغییر enabled به‌ندرت رخ می‌دهد، پس recomposition اینجا مشکلی نیست
    val contentAlpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.45f,
        label = "content_alpha"
    )

    val surfaceBrush = remember(palette.surface) {
        Brush.verticalGradient(palette.surface)
    }
    val borderBrush = remember {
        Brush.verticalGradient(
            listOf(
                Color.White.copy(alpha = 0.28f),
                Color.White.copy(alpha = 0.10f),
                Color.Transparent
            )
        )
    }
    val ambientShadow = remember { Color.Black.copy(alpha = 0.55f) }
    val spotShadow = remember(palette.spot) { palette.spot.copy(alpha = 0.6f) }

    Box(
        modifier = modifier
            // 58 + 6 → لایه ضخامت از والد بیرون نمی‌زند
            .height(ButtonSurfaceHeight + ButtonDepth)
            .clickable(
                enabled = enabled,
                interactionSource = interaction,
                indication = null,
                role = Role.Button,          // ✅ Accessibility
                onClick = onClick
            )
    ) {

        /*
         * لایه ضخامت سه‌بعدی
         */
        Box(
            Modifier
                .fillMaxWidth()
                .height(ButtonSurfaceHeight)
                .offset(y = ButtonDepth)
                .alpha(contentAlpha)
                .background(
                    palette.edge,
                    shape
                )
        )

        /*
         * بدنه اصلی
         *
         * translationY + shadowElevation + alpha همه در یک graphicsLayer:
         * انیمیشن فشار بدون recomposition و بدون re-layout.
         * clip = true باعث می‌شود highlight گوشه‌های گرد را خراب نکند.
         */
        Box(
            Modifier
                .fillMaxWidth()
                .height(ButtonSurfaceHeight)
                .graphicsLayer {
                    translationY = pressTranslation.value.toPx()
                    shadowElevation = pressElevation.value.toPx()
                    this.shape = shape
                    clip = true
                    ambientShadowColor = ambientShadow
                    spotShadowColor = spotShadow
                    alpha = contentAlpha          // متن و آیکون هم محو می‌شوند
                }
                .background(
                    surfaceBrush,
                    shape
                )
                .topHighlight(alpha = highlightAlpha)   // زیر محتوا، روی پس‌زمینه
            .border(
                1.dp,
                borderBrush,
                shape
            ),
            contentAlignment = Alignment.Center) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                content = content
            )
        }
    }
}

@Preview(
    name = "Dialog Preview",
    showBackground = false,
)
@Composable
fun Base3DButtonPreview() {

    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = false) {
        // ✅ مستقیماً CustomDialog را صدا بزن

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
            enabled = true,
            onClick = {},
        ) {

        }
    }
}