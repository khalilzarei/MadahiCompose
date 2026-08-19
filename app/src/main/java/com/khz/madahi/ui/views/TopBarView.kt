package com.khz.madahi.ui.views

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.khz.madahi.ui.theme.MadahiTheme

@Composable
fun TopBarView(
    onSearchClick: () -> Unit,
    onSettingClick: () -> Unit,
    function: @Composable () -> Unit,
) {
    ImageBackgroundCard() {
        function()
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TopBarViewPreviewDark() {
    MadahiTheme(darkTheme = true) {
        TopBarView(
            onSettingClick = {},
            onSearchClick = {},
        ) {
            Text(
                text = "Hello",
                color = Color.White,
                fontSize = 28.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}