package com.khz.madahi.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.textPrimary

@Composable
fun AddDialogScreen(
    beforeIndex: Int,
    afterIndex: Int,
    text: String,
    onTextChange: (String) -> Unit,
    onBeforeIncrease: () -> Unit,
    onBeforeDecrease: () -> Unit,
    onAfterIncrease: () -> Unit,
    onAfterDecrease: () -> Unit,
    onAdd: () -> Unit,
    onBack: () -> Unit
) {
    val colors = LocalMadahiColors.current

    MadahiBackground {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp)
        ) {

            TopTitleBar(
                title = "افزودن دیالوگ",
                onBack = onBack
            )

            Spacer(Modifier.height(22.dp))

            GlassCard(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(24.dp)
                ) {

                    Text(
                        text = "متن دیالوگ",
                        color = colors.textPrimary,
                        fontSize = 17.sp
                    )

                    Spacer(Modifier.height(12.dp))

                    GlassTextField(
                        value = text,
                        onValueChange = onTextChange,
                        minHeight = 150.dp
                    )

                    Spacer(Modifier.height(24.dp))

                    PositionSelector(
                        title = "قبل از بیت",
                        value = beforeIndex,
                        onIncrease = onBeforeIncrease,
                        onDecrease = onBeforeDecrease
                    )

                    Spacer(Modifier.height(14.dp))

                    PositionSelector(
                        title = "بعد از بیت",
                        value = afterIndex,
                        onIncrease = onAfterIncrease,
                        onDecrease = onAfterDecrease
                    )
                }
            }

            Spacer(Modifier.height(22.dp))

            ThreeDButton(
                text = "افزودن دیالوگ",
                modifier = Modifier.fillMaxWidth(),
                onClick = onAdd
            )
        }
    }
}

@Preview(
    name = "Dialog Preview",
    showBackground = false,
)
@Composable
fun AddDialogScreenPreview() {

    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = true) {
        // ✅ مستقیماً CustomDialog را صدا بزن
        AddDialogScreen(
            beforeIndex = 0,
            afterIndex = 2,
            text = ": String",
            onTextChange = {},
            onBeforeIncrease = {},
            onBeforeDecrease = {},
            onAfterIncrease = {},
            onAfterDecrease = {},
            onAdd = {},
            onBack = {},
        )
    }
}