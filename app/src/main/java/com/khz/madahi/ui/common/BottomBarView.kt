// ui/category/BottomBarView.kt
package com.khz.madahi.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.R
import com.khz.madahi.ui.theme.BottomBarPalette
import com.khz.madahi.ui.theme.MadahiTheme
import com.khz.madahi.ui.theme.getBottomBarColors

// ============ تب‌های پایین ============
enum class BottomTab {
    MESSAGE,
    PROFILE,
    CATEGORY,
    FAVORITES,
    SETTINGS,
    ABOUT
}

data class BottomBarActions(
    val onBackClick: () -> Unit = {},
    val onHomeClick: () -> Unit = {},   // اکشن دکمه‌ی وسط (FAB)
    val onCategoryClick: () -> Unit = {},
    val onFavoritesClick: () -> Unit = {},
    val onSettingsClick: () -> Unit = {},
    val onProfileClick: () -> Unit = {},
    val onAboutClick: () -> Unit = {},
    val onMessageClick: () -> Unit = {},
)

// ============ شکل سفارشی نوار پایین با فرورفتگی وسط ============
// این کلاس همان کاری را انجام می‌دهد که در مقاله با Canvas + Shape توضیح داده شد:
// یک Path می‌کشد که علاوه بر گوشه‌های گرد بالا، یک کمان (Cutout) برای جا دادن FAB دارد.
class BottomNavCutoutShape(
    private val cornerRadius: Dp = 10.dp,
    private val cutoutRadius: Dp = 32.dp,
    private val cutoutMargin: Dp = 6.dp
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val cornerRadiusPx = with(density) { cornerRadius.toPx() }
        val cutoutRadiusPx = with(density) { cutoutRadius.toPx() }
        val cutoutMarginPx = with(density) { cutoutMargin.toPx() }
        val totalCutoutRadiusPx = cutoutRadiusPx + cutoutMarginPx
        val centerX = size.width / 2f

        val path = Path().apply {
            // گوشه‌ی بالا-چپ
            moveTo(
                0f,
                cornerRadiusPx
            )
            quadraticBezierTo(
                0f,
                0f,
                cornerRadiusPx,
                0f
            )

            // خط تا شروع فرورفتگی
            lineTo(
                centerX - totalCutoutRadiusPx,
                0f
            )

            // کمان فرورفتگی (Cutout) برای FAB
            arcTo(
                rect = Rect(
                    left = centerX - totalCutoutRadiusPx,
                    top = -totalCutoutRadiusPx,
                    right = centerX + totalCutoutRadiusPx,
                    bottom = totalCutoutRadiusPx
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = -180f,
                forceMoveTo = false
            )

            // خط تا گوشه‌ی بالا-راست
            lineTo(
                size.width - cornerRadiusPx,
                0f
            )
            quadraticBezierTo(
                size.width,
                0f,
                size.width,
                cornerRadiusPx
            )

            // پایین نوار
            lineTo(
                size.width,
                size.height
            )
            lineTo(
                0f,
                size.height
            )
            close()
        }

        return Outline.Generic(path)
    }
}

@Composable
fun BottomBarView(
    selectedTab: BottomTab = BottomTab.CATEGORY,
    actions: BottomBarActions = BottomBarActions()
) {
    val colors = getBottomBarColors()
    val cutoutShape = remember {
        BottomNavCutoutShape(
            cornerRadius = 10.dp,
            cutoutRadius = 32.dp,
            cutoutMargin = 6.dp
        )
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        // ---------- خود نوار پایین با فرورفتگی وسط ----------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(colors = colors.background),
                    shape = cutoutShape
                )
                .border(
                    width = 2.dp,
                    color = colors.border,
                    shape = cutoutShape
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // ---- سه آیتم سمت راست ----
                BottomBarItem(
                    icon = Icons.Default.GridView,
                    label = "دسته‌بندی",
                    selected = selectedTab == BottomTab.CATEGORY,
                    colors = colors,
                    onClick = actions.onCategoryClick,
                    modifier = Modifier.weight(1f)
                )

                BottomBarItem(
                    icon = if (selectedTab == BottomTab.FAVORITES) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    label = "علاقه‌مندی",
                    selected = selectedTab == BottomTab.FAVORITES,
                    colors = colors,
                    onClick = actions.onFavoritesClick,
                    modifier = Modifier.weight(1f)
                )

                BottomBarItem(
                    icon = Icons.Default.Settings,
                    label = "تنظیمات",
                    selected = selectedTab == BottomTab.SETTINGS,
                    colors = colors,
                    onClick = actions.onSettingsClick,
                    modifier = Modifier.weight(1f)
                )

                // ---- فاصله‌ی خالی زیر FAB ----
                Spacer(modifier = Modifier.weight(1f))

                // ---- سه آیتم سمت چپ ----
                BottomBarItem(
                    icon = Icons.AutoMirrored.Filled.Message,
                    label = "پیام ها",
                    selected = selectedTab == BottomTab.MESSAGE,
                    colors = colors,
                    onClick = actions.onMessageClick,
                    modifier = Modifier.weight(1f)
                )

                BottomBarItem(
                    icon = Icons.Default.Person,
                    label = "پروفایل",
                    selected = selectedTab == BottomTab.PROFILE,
                    colors = colors,
                    onClick = actions.onProfileClick,
                    modifier = Modifier.weight(1f)
                )

                BottomBarItem(
                    icon = Icons.Default.Info,
                    label = "درباره ما",
                    selected = selectedTab == BottomTab.ABOUT,
                    colors = colors,
                    onClick = actions.onAboutClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        FloatingActionButton(
            onClick = actions.onHomeClick,
            containerColor = Color(0xFF074634),
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .offset(y = (-38).dp)
                .size(70.dp)
                .border(
                    width = 3.dp,
                    color = Color(0xFFD4AF37),  // طلایی برای حاشیه
                    shape = CircleShape
                )
        ) {
            Image(
                painter = painterResource(
                    R.drawable.ic_fab_border // ✅ تصویر دارک
                ),
                contentDescription = "Splash Background",
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.FillBounds

            )
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "افزودن به علاقه‌مندی‌ها",
                modifier = Modifier.size(36.dp),
                tint = Color.White
            )
        }
    }
}

// ============ آیتم منوی پایین ============
@Composable
private fun BottomBarItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    colors: BottomBarPalette,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.padding(
                10.dp
            ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) colors.selectedIcon else colors.unselectedIcon,
                modifier = Modifier.size(30.dp)
            )
        }

        Text(
            modifier = Modifier.padding(bottom = 3.dp),
            text = label,
            fontSize = if (selected) 15.sp else 13.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Thin,
            color = if (selected) colors.selectedText else colors.unselectedText
        )
    }
}

// ============ Preview ============
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun CategoryBottomBarDarkPreview() {
    MadahiTheme(darkTheme = false) {
        BottomBarView(
            selectedTab = BottomTab.PROFILE,
            actions = BottomBarActions()
        )
    }
}

//@Preview(
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_NO
//)
//@Composable
//fun CategoryBottomBarLightPreview() {
//    MadahiTheme(darkTheme = false) {
//        CategoryBottomBar(
//            selectedTab = BottomTab.SETTINGS,
//            onFavoritesClick = {},
//            onSettingsClick = {})
//    }
//}