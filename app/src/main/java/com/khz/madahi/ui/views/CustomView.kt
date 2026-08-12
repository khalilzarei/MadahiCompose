// ui/views/ImageBackgroundCard.kt
package com.khz.madahi.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khz.madahi.R
import com.khz.madahi.ui.theme.Gold
import com.khz.madahi.ui.theme.PrimaryGreen
import com.khz.madahi.ui.theme.PrimaryGreenDark

@Composable
fun ImageBackgroundCard(
    modifier: Modifier = Modifier,
    height: Int = 65,
    content: @Composable () -> Unit
) {

    val corner = painterResource(R.drawable.ic_kenar)
    val size = 60.dp
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(10.dp)
    ) {
        Image(
            painter = corner,
            contentDescription = null,
            modifier = Modifier
                .width(size / 2)
                .height(size)
                .rotate(180f),
            contentScale = ContentScale.FillBounds
        )
        Box(
            modifier = Modifier
                .height(size)
                .weight(1f)
                .background(
                    shape = RoundedCornerShape(10.dp),
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            PrimaryGreenDark.copy(alpha = 0.8f),
                            PrimaryGreenDark.copy(alpha = 0.3f),
                            PrimaryGreenDark.copy(alpha = 0.8f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    color = Color(0xFFFFC107),
                    shape = RoundedCornerShape(10.dp) // shape را به border بدهید
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    "Hello",
                    color = Gold
                )
            }
        }
        Image(
            painter = corner,
            contentDescription = null,
            modifier = Modifier
                .width(size / 2)
                .height(size),
            contentScale = ContentScale.FillBounds
        )
    }

}

@Preview(showBackground = false)
@Composable
fun ImageBackgroundCardPreview() {
    MaterialTheme {
        ImageBackgroundCard {}
    }
}