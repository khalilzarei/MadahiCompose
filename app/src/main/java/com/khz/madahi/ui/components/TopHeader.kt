package com.khz.madahi.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.primaryLight
import com.khz.madahi.ui.theme.textSecondary

@Composable
fun TopHeader(
    title: String,
    subtitle: String,
    onHeaderBottonClicked: () -> Unit
) {
    val colors = LocalMadahiColors.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                24.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                color = colors.primaryLight,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    color = colors.textSecondary,
                    fontSize = 14.sp,
                )
            }
        }

        GlassIconButton(
            text = "☼",
            onClick = onHeaderBottonClicked
        )
    }
}

@Preview(
    name = "Dialog Preview",
    showBackground = false,
)
@Composable
fun TopHeaderPreview() {

    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = true) {
        // ✅ مستقیماً CustomDialog را صدا بزن
        TopHeader(
            title = "title",
            subtitle = "",
            onHeaderBottonClicked = {},
        )
    }
}