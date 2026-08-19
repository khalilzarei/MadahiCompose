package com.khz.madahi.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
    onClick: () -> Unit
) {
    val colors = LocalMadahiColors.current

    val interactionSource = remember {
        MutableInteractionSource()
    }

    val isPressed by interactionSource.collectIsPressedAsState()

    val offset by animateDpAsState(
        targetValue = if (isPressed) 4.dp else 0.dp,
        label = "gold_offset"
    )

    val elevation by animateDpAsState(
        targetValue = if (isPressed) 4.dp else 14.dp,
        label = "gold_elevation"
    )

    val shape = RoundedCornerShape(18.dp)

    Box(
        modifier = modifier
            .height(62.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {

        // ضخامت پایین
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .offset(y = 6.dp)
                .background(
                    colors.gold.copy(alpha = 0.45f),
                    shape
                )
        )

        // سطح اصلی
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .offset(y = offset)
                .shadow(
                    elevation = elevation,
                    shape = shape,
                    ambientColor = Color.Black.copy(alpha = 0.55f),
                    spotColor = colors.gold.copy(alpha = 0.45f)
                )
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            colors.goldLight,
                            colors.gold,
                            colors.gold.copy(alpha = 0.78f)
                        )
                    ),
                    shape
                )
                .border(
                    1.dp,
                    Color.White.copy(alpha = 0.25f),
                    shape
                ),
            contentAlignment = Alignment.Center
        ) {

            // انعکاس نور
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(25.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.25f),
                                Color.Transparent
                            )
                        ),
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp
                        )
                    )
            )

            Text(
                text = text,
                color = colors.textPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(
    name = "Dialog Preview",
    showBackground = false,
)
@Composable
fun Gold3DButtonPreview() {

    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = false) {
        // ✅ مستقیماً CustomDialog را صدا بزن
        Gold3DButton(
            text = "ThreeDButton",
            onClick = {},
        )
    }
}

@Preview(
    name = "Dialog Preview",
    showBackground = false,
)
@Composable
fun Gold3DButtonPreviewDark() {

    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = true) {
        // ✅ مستقیماً CustomDialog را صدا بزن
        Gold3DButton(
            text = "ThreeDButton",
            onClick = {},
        )
    }
}