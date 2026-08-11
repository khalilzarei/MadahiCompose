// ui/common/DecorativeCorners.kt
package com.khz.madahi.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khz.madahi.R

/**
 * چهار گوشه تزئینی corner.png روی گوشه‌های یک Box.
 * باید داخل BoxScope (یعنی داخل یک Box) فراخوانی شود.
 *
 * مثال:
 * Box(modifier = Modifier.background(...).border(...)) {
 *     DecorativeCorners()
 *     // ... محتوای دیگر
 * }
 */
@Composable
fun BoxScope.DecorativeCorners(
    cornerSize: Dp = 40.dp,
    edgePadding: Dp = 1.dp,
    topPadding: Dp = 3.dp,
    bottomExtraPadding: Dp = 2.dp
) {
    // گوشه بالا-چپ
    Box(
        modifier = Modifier
            .align(Alignment.TopStart)
            .size(cornerSize)
    ) {
        Image(
            painter = painterResource(R.drawable.corner),
            contentDescription = "Corner",
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

    // گوشه بالا-راست
    Box(
        modifier = Modifier
            .align(Alignment.TopEnd)
            .size(cornerSize)
    ) {
        Image(
            painter = painterResource(R.drawable.corner),
            contentDescription = "Corner",
            modifier = Modifier
                .size(cornerSize)
                .padding(
                    end = edgePadding,
                    top = topPadding
                )
                .rotate(180f),
            contentScale = ContentScale.FillBounds
        )
    }

    // گوشه پایین-چپ
    Box(
        modifier = Modifier
            .align(Alignment.BottomStart)
            .size(cornerSize)
    ) {
        Image(
            painter = painterResource(R.drawable.corner),
            contentDescription = "Corner",
            modifier = Modifier
                .size(cornerSize)
                .padding(
                    start = edgePadding,
                    bottom = topPadding + bottomExtraPadding
                ),
            contentScale = ContentScale.FillBounds
        )
    }

    // گوشه پایین-راست
    Box(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .size(cornerSize)
    ) {
        Image(
            painter = painterResource(R.drawable.corner),
            contentDescription = "Corner",
            modifier = Modifier
                .size(cornerSize)
                .rotate(90f)
                .padding(
                    end = edgePadding + bottomExtraPadding,
                    bottom = topPadding + bottomExtraPadding
                ),
            contentScale = ContentScale.FillBounds
        )
    }
}