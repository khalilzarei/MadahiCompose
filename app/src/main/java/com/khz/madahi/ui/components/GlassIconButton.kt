// ui/components/GlassIconButton.kt
package com.khz.madahi.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.textPrimary

// ============================================================
// دکمه آیکونی شیشه‌ای سه‌بعدی
// ------------------------------------------------------------
// مثل Mini3DButton ولی دایره‌ای و با اندازه قابل تنظیم.
// با فشردن، دکمه به پایین فرو می‌رود (جلوه سه‌بعدی).
// ============================================================

@Composable
fun GlassIconButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color? = null,               // null → رنگ متن تم
    size: Dp = 52.dp,
    shape: Shape = CircleShape
) {
    val colors = LocalMadahiColors.current

    val interactionSource = remember {
        MutableInteractionSource()
    }

    val pressed by interactionSource.collectIsPressedAsState()

    // فرو رفتن دکمه هنگام لمس (جلوه سه‌بعدی)
    val offset by animateDpAsState(
        targetValue = if (pressed) 3.dp else 0.dp,
        label = "glass_icon_offset"
    )

    GlassCard3D(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                translationY = offset.toPx()
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = shape) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                tint = tint
                        ?: colors.textPrimary,
                modifier = Modifier.size(size * 0.45f)
            )
        }
    }
}

@Preview(
    name = "Dialog Preview",
    showBackground = false,
)
@Composable
fun GlassIconButtonPreview() {
    MadahiThemeGreen(darkTheme = true) {
        val colors = LocalMadahiColors.current
        GlassIconButton(
            imageVector = androidx.compose.material.icons.Icons.Default.Add,
            tint = colors.gold,
            onClick = {},
        )
    }
}
