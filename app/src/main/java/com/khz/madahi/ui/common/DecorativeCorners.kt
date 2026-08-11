// ui/common/DecorativeCorners.kt
package com.khz.madahi.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khz.madahi.R

/**
 * چهار گوشه تزئینی (corner.png) که در گوشه‌های یک Box قرار می‌گیرند.
 * باید داخل یک Box با اندازه مشخص (parent) استفاده شود.
 */
@Composable
fun BoxScopeDecorativeCorners(
    cornerSize: Dp = 40.dp,
    edgePadding: Dp = 1.dp,
    topPadding: Dp = 3.dp,
    bottomPadding: Dp = 5.dp
) {
    // گوشه بالا-چپ
    Box(
        modifier = Modifier.size(cornerSize)
    ) {
        Image(
            painter = painterResource(R.drawable.corner),
            contentDescription = null,
            modifier = Modifier
                .size(cornerSize)
                .padding(
                    start = edgePadding,
                    top = topPadding
                )
                .rotate(-90f),
            contentScale = ContentScale.FillBounds
        )
    }
}