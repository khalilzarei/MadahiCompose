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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
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
import com.khz.madahi.ui.theme.textPrimary

@Composable
fun Delete3DButton(
    text: String = "حذف",
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val colors = LocalMadahiColors.current

    val interactionSource = remember {
        MutableInteractionSource()
    }

    val isPressed by interactionSource.collectIsPressedAsState()

    val surfaceOffset by animateDpAsState(
        targetValue = if (isPressed) 4.dp else 0.dp,
        label = "delete_button_offset"
    )

    val shadowElevation by animateDpAsState(
        targetValue = if (isPressed) 4.dp else 12.dp,
        label = "delete_button_shadow"
    )

    val alpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.45f,
        label = "delete_button_alpha"
    )

    val shape = RoundedCornerShape(18.dp)

    // رنگ‌های مخصوص دکمه حذف
    val deleteLight = Color(0xFFE85D5D)
    val deletePrimary = Color(0xFFC62828)
    val deleteDark = Color(0xFF7F1717)

    Box(
        modifier = modifier
            .height(62.dp)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {

        /*
         * =====================================
         * لایه ضخامت سه بعدی
         * =====================================
         */

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .offset(y = 6.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            deleteDark,
                            deleteDark.copy(alpha = 0.75f)
                        )
                    ),
                    shape = shape
                )
        )

        /*
         * =====================================
         * بدنه اصلی
         * =====================================
         */

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .offset(y = surfaceOffset)
                .shadow(
                    elevation = shadowElevation,
                    shape = shape,
                    ambientColor = Color.Black.copy(alpha = 0.55f),
                    spotColor = deleteDark.copy(alpha = 0.65f)
                )
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            deleteLight.copy(alpha = alpha),
                            deletePrimary.copy(alpha = alpha),
                            deleteDark.copy(alpha = alpha)
                        )
                    ),
                    shape = shape
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.28f),
                            deleteLight.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    ),
                    shape = shape
                ),
            contentAlignment = Alignment.Center
        ) {

            /*
             * =====================================
             * Highlight شیشه‌ای
             * =====================================
             */

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(25.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.16f),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp
                        )
                    )
            )

            /*
             * =====================================
             * محتوا
             * =====================================
             */

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = null,
                    tint = colors.textPrimary.copy(alpha = alpha),
                    modifier = Modifier.size(23.dp)
                )

                Spacer(
                    modifier = Modifier.width(9.dp)
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
}

@Preview(
    name = "Dialog Preview",
    showBackground = false,
)
@Composable
fun Delete3DButtonPreview() {

    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = false) {

        Delete3DButton(
            text = "Delete",
            onClick = {},
        )
    }
}

@Preview(
    name = "Dialog Preview",
    showBackground = false,
)
@Composable
fun Delete3DButtonPreviewDark() {

    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = true) {

        Delete3DButton(
            text = "Delete",
            onClick = {},
        )
    }
}