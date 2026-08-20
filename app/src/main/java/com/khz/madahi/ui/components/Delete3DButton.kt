package com.khz.madahi.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.delete
import com.khz.madahi.ui.theme.deleteEdge
import com.khz.madahi.ui.theme.deleteLight
import com.khz.madahi.ui.theme.textPrimary

@Composable
fun Delete3DButton(
    text: String = "حذف",
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val c = LocalMadahiColors.current
    Base3DButton(
        palette = Button3DPalette(
            c.deleteEdge,
            listOf(
                c.deleteLight,
                c.delete,
                c.deleteEdge
            ),
            c.deleteEdge
        ),
        modifier = modifier,
        enabled = enabled,
        onClick = onClick
    ) {
//        Icon(
//            Icons.Outlined.Delete,
//            null,
//            tint = c.textPrimary,
//            modifier = Modifier.size(23.dp)
//        )
//        Spacer(Modifier.width(9.dp))
        Text(
            text,
            color = c.textPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
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