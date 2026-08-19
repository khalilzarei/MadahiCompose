// ui/views/ImageBackgroundCard.kt
package com.khz.madahi.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.R
import com.khz.madahi.ui.theme.PrimaryGreenDark

@Composable
fun ImageBackgroundCard(
    function: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(10.dp),
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF07130c).copy(0.9f),
                        PrimaryGreenDark.copy(0.5f),
                        Color(0xFF07130c).copy(0.5f),
                    )
                )
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.top_title),
            contentDescription = null,
            modifier = Modifier// ✅ این خط موقعیت تصویر را بالا سمت راست قرار می‌دهد
                .height(45.dp)
                .padding(horizontal = 20.dp),
            contentScale = ContentScale.FillBounds
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {

            // محتوای متنی وسط یا داخل کارت
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center), // برای چیدمان بهتر محتوا
            ) {
                Row() {

                    Box(
                        modifier = Modifier
                            .height(80.dp)
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        function()

                    }

                }
            }
            // ۱. تصویر گوشه بالا سمت راست

            // ۱. تصویر گوشه بالا سمت راست
            Image(
                painter = painterResource(id = R.drawable.corner),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopStart) // ✅ این خط موقعیت تصویر را بالا سمت راست قرار می‌دهد
                    .rotate(270f)
                    .size(130.dp),
                contentScale = ContentScale.FillBounds
            )

            // ۲. تصویر گوشه بالا سمت چپ
            Image(
                painter = painterResource(id = R.drawable.corner),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd) // ✅ این خط موقعیت تصویر را بالا سمت چپ قرار می‌دهد
                    .rotate(180f)
                    .size(130.dp),
                contentScale = ContentScale.FillBounds
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun ImageBackgroundCardPreview() {
    MaterialTheme {
        ImageBackgroundCard() {
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