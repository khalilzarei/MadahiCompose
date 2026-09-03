// ui/booklet/BookletScrollbar.kt
package com.khz.madahi.ui.booklet

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.gold

/**
 * نوار اسکرول باریک طلایی برای لیست‌های بلند کتابچه.
 * موقعیت و اندازه‌ی آن بر اساس آیتم‌های قابل مشاهده در LazyListState رسم می‌شود.
 */
@Composable
fun BookletScrollbar(
    state: LazyListState,
    modifier: Modifier = Modifier
) {
    val colors = LocalMadahiColors.current
    val info = state.layoutInfo
    val total = info.totalItemsCount
    val visible = info.visibleItemsInfo

    if (total <= 0 || visible.isEmpty()) return

    val firstIndex = visible.first().index
    val lastIndex = visible.last().index
    val visibleCount = (lastIndex - firstIndex + 1).coerceAtLeast(1)

    val thumbFraction = visibleCount.toFloat() / total.toFloat()
    val scrollFraction = firstIndex.toFloat() / total.toFloat()

    Canvas(modifier = modifier) {
        val trackHeight = size.height
        if (trackHeight <= 0f) return@Canvas

        val thumbHeight = (trackHeight * thumbFraction).coerceAtLeast(24.dp.toPx())
        val thumbY = scrollFraction * (trackHeight - thumbHeight)

        drawRoundRect(
            color = colors.gold.copy(alpha = 0.35f),
            topLeft = Offset(
                0f,
                thumbY
            ),
            size = Size(
                size.width,
                thumbHeight
            ),
            cornerRadius = CornerRadius(
                size.width / 2f,
                size.width / 2f
            )
        )
    }
}
