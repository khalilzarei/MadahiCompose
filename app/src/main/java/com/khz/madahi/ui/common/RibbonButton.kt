// ui/common/RibbonButton.kt
package com.khz.madahi.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.khz.madahi.ui.theme.RibbonButtonColors

@Composable
fun RibbonButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDanger: Boolean = false
) {
    val gradient = if (isDanger) RibbonButtonColors.DeleteGradient else RibbonButtonColors.SaveGradient

    Box(
        modifier = modifier
            .height(46.dp)
            .clip(RibbonShape)
            .background(Brush.verticalGradient(gradient))
            .border(
                2.dp,
                RibbonButtonColors.Border,
                RibbonShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // ✅ نگین طلایی سمت راست
        DiamondOrnament(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 14.dp)
        )
        // ✅ نگین طلایی سمت چپ
        DiamondOrnament(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 14.dp)
        )

        Text(
            text = text,
            color = RibbonButtonColors.TextLight,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DiamondOrnament(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(10.dp)
            .rotate(45f)
            .border(
                1.dp,
                RibbonButtonColors.Ornament
            )
    )
}

val RibbonShape = GenericShape { size, _ ->
    val w = size.width
    val h = size.height
    val cut = h * 0.32f // میزان نوک‌تیزی چپ و راست

    moveTo(
        cut,
        0f
    )
    lineTo(
        w - cut,
        0f
    )
    lineTo(
        w,
        h / 2f
    )
    lineTo(
        w - cut,
        h
    )
    lineTo(
        cut,
        h
    )
    lineTo(
        0f,
        h / 2f
    )
    close()
}

//
//
//@Preview(
//    name = "Category Dialog - Light",
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_NO
//)
//@Composable
//fun RibbonButtonPreview() {
//    MadahiTheme(darkTheme = false) {
//        RibbonButton(
//            text = "انصراف",
//            onClick = {},
//            modifier = Modifier.fillMaxWidth(),
//            isDanger = true
//        )
//    }
//}