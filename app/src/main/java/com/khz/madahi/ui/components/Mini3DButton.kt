package com.khz.madahi.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.TextPrimary

@Composable
fun Mini3DButton(
    imageVector: ImageVector = Icons.Outlined.Delete,
    onClick: () -> Unit,
    tint: Color = TextPrimary
) {
    val colors = LocalMadahiColors.current

    val interactionSource = remember {
        MutableInteractionSource()
    }

    val pressed by interactionSource.collectIsPressedAsState()

    val offset by animateDpAsState(
        targetValue = if (pressed) 3.dp else 0.dp,
        label = "mini_offset"
    )

    val shape = RoundedCornerShape(15.dp)

    GlassCard3D(
        modifier = Modifier
            .size(54.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
        }

    }
}

@Preview(
    name = "Dialog Preview",
    showBackground = false,
)
@Composable
fun Mini3DButtonPreview() {

    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = true) {
        // ✅ مستقیماً CustomDialog را صدا بزن
        Mini3DButton(
            onClick = {},
        )
    }
}