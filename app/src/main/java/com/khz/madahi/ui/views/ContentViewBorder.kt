package com.khz.madahi.ui.views

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khz.madahi.R
import com.khz.madahi.ui.theme.MadahiTheme

private val GoldLight = Color(0xFFFFE082)

@Composable
fun ContentViewBorder(
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left decorative border column
            SideBorderColumn(
                modifier = Modifier.padding(horizontal = 14.dp)
            )

            // Main Content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(horizontal = 14.dp)
            ) {
                content()
            }

            // Right decorative border column (Flipped horizontally for symmetry)
            SideBorderColumn(
                modifier = Modifier.graphicsLayer(scaleX = -1f)

            )
        }
    }
}

@Composable
private fun SideBorderColumn(
    modifier: Modifier = Modifier,
    repeatCount: Int = 8
) {
    Column(
        modifier = modifier.fillMaxHeight()
    ) {
        repeat(repeatCount) {
            SideImageBorder(
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SideImageBorder(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.ic_side),
        contentDescription = null,
        modifier = modifier
            .fillMaxHeight()
            .width(70.dp),
        contentScale = ContentScale.FillBounds
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ContentViewBorderPreview() {
    MadahiTheme(darkTheme = true) {
        ContentViewBorder {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "دفتر مداحی",
                    color = GoldLight
                )
            }
        }
    }
}