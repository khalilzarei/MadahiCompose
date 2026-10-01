package com.khz.madahi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.border
import com.khz.madahi.ui.theme.delete
import com.khz.madahi.ui.theme.surfaceGlass
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary

@Composable
fun GlassTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    minHeight: Dp = 58.dp,
    keyboardType: KeyboardType = KeyboardType.Text,
    hasError: Boolean = false,
    hint: String = "",
    // ✅ ورودی رمز عبور: متن پنهان می‌شود و دکمهٔ نمایش/پنهان دارد
    isPassword: Boolean = false,
) {
    val colors = LocalMadahiColors.current

    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    // چون متن وسط‌چین است، برای دکمهٔ چشم از دو طرف فاصلهٔ مساوی می‌گذاریم
    // تا مرکز متن جابه‌جا نشود.
    val horizontalPadding = if (isPassword) 46.dp else 14.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                colors.surfaceGlass.copy(alpha = 0.55f),
                RoundedCornerShape(18.dp)
            )
            .border(
                1.dp,
                if (hasError) colors.delete else colors.border,
                RoundedCornerShape(18.dp)
            )
    ) {

        // ✅ هینت — فقط وقتی فیلد خالی است نمایش داده می‌شود
        if (value.isEmpty() && hint.isNotEmpty()) {
            Text(
                text = hint,
                color = colors.textMuted,
                fontSize = 15.sp,
                lineHeight = 30.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            )
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = horizontalPadding,
                    end = horizontalPadding,
                    top = 14.dp,
                    bottom = 14.dp
                ),
            textStyle = TextStyle(
                color = colors.textPrimary,
                fontSize = 17.sp,
                lineHeight = 30.sp,
                textAlign = TextAlign.Center
            ),
            singleLine = isPassword,
            visualTransformation = if (isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPassword) KeyboardType.Password else keyboardType
            )
        )

        // ✅ دکمهٔ نمایش/پنهان‌کردن رمز
        if (isPassword) {
            IconButton(
                onClick = { passwordVisible = !passwordVisible },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 4.dp)
                    .size(38.dp)
            ) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = if (passwordVisible) "پنهان‌کردن رمز عبور" else "نمایش رمز عبور",
                    tint = colors.textMuted,
                    modifier = Modifier.size(20.dp)
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
fun GlassTextFieldPreview() {

    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = true) {
        // ✅ مستقیماً CustomDialog را صدا بزن
        GlassTextField(
            value = "String",
            onValueChange = {},
            minHeight = 58.dp,
            keyboardType = KeyboardType.Number
        )
    }
}

@Preview(
    name = "Password Field Preview",
    showBackground = false,
)
@Composable
fun GlassPasswordFieldPreview() {
    MadahiThemeGreen(darkTheme = true) {
        GlassTextField(
            value = "123456",
            onValueChange = {},
            isPassword = true,
            hint = "رمز عبور"
        )
    }
}
