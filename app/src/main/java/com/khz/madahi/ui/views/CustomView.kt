// ui/views/ImageBackgroundCard.kt
package com.khz.madahi.ui.views

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khz.madahi.R

@Composable
fun ImageBackgroundCard(
    modifier: Modifier = Modifier,
    height: Int = 65,
    content: @Composable () -> Unit
) {
    val isDarkTheme = MaterialTheme.colorScheme.background == Color(0xFF0B0B0B) || MaterialTheme.colorScheme.background == Color(0xFF1A1A1A)

    val background = if (isDarkTheme) {
        R.drawable.bg_item_back_dark
    } else {
        R.drawable.bg_item_back_dark
    }

    DrawableBackgroundView(
        backgroundRes = background,
        modifier = Modifier.fillMaxWidth()
            .height(65.dp)
    ) {
        Text(
            text = "محتوای ویو",
            modifier = Modifier.align(Alignment.Center),
            color = Color.White
        )
    }
}

@Composable
fun DrawableBackgroundView(
    @DrawableRes backgroundRes: Int,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {}
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        content()
    }
}

@Preview(showBackground = false)
@Composable
fun ImageBackgroundCardPreview() {
    MaterialTheme {
        ImageBackgroundCard {}
    }
}