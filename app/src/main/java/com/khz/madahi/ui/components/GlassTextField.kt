package com.khz.madahi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.border
import com.khz.madahi.ui.theme.surfaceGlass
import com.khz.madahi.ui.theme.textPrimary

@Composable
fun GlassTextField(
    value: String,
    onValueChange: (String) -> Unit,
    minHeight: Dp = 58.dp
) {
    val colors = LocalMadahiColors.current

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = minHeight)
            .background(
                colors.surfaceGlass.copy(alpha = 0.55f),
                RoundedCornerShape(18.dp)
            )
            .border(
                1.dp,
                colors.border,
                RoundedCornerShape(18.dp)
            )
            .padding(18.dp),
        textStyle = TextStyle(
            color = colors.textPrimary,
            fontSize = 17.sp,
            lineHeight = 30.sp
        )
    )
}

@Preview(
    name = "Dialog Preview",
    showBackground = false,
)
@Composable
fun GlassTextFieldPreview() {

    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = true) {
        // ✅ مستقیماً CustomDialog را صدا بزن
        GlassTextField(
            value = "String",
            onValueChange = {},
            minHeight = 58.dp,
        )
    }
}