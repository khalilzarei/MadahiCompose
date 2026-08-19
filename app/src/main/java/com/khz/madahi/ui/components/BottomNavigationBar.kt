package com.khz.madahi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.ui.common.BottomBarActions
import com.khz.madahi.ui.common.BottomTab
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.textSecondary

@Composable
fun BottomNavigationBar(
    selectedTab: BottomTab = BottomTab.CATEGORY,
    actions: BottomBarActions = BottomBarActions(),
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            BottomNavItem(
                title = "علاقه‌مندی‌ها",
                selected = selectedTab == BottomTab.FAVORITES,
                onClick = actions.onFavoritesClick
            )

            BottomNavItem(
                title = "دسته‌بندی",
                selected = selectedTab == BottomTab.CATEGORY,
                onClick = actions.onCategoryClick
            )

            BottomNavItem(
                title = "تنظیمات",
                selected = selectedTab == BottomTab.SETTINGS,
                onClick = actions.onSettingsClick
            )

            BottomNavItem(
                title = "درباره ما",
                selected = selectedTab == BottomTab.ABOUT,
                onClick = actions.onAboutClick
            )

            BottomNavItem(
                title = "پیام ها",
                selected = selectedTab == BottomTab.MESSAGE,
                onClick = actions.onAboutClick
            )
        }
    }
}

@Composable
fun BottomNavItem(
    title: String,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    val colors = LocalMadahiColors.current

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (selected) colors.primary.copy(alpha = 0.35f)
                else Color.Transparent
            )
            .clickable { onClick() }
            .padding(
                horizontal = 22.dp,
                vertical = 10.dp
            )) {

        Text(
            text = title,
            color = colors.textSecondary,
            fontSize = 13.sp
        )
    }
}

@Preview(
    name = "Dialog Preview",
    showBackground = false,
)
@Composable
fun BottomNavItemPreview() {

    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = false) {

        BottomNavItem(
            title = "title",
            selected = true,
            onClick = {},
        )
    }
}

@Preview(
    name = "Dialog Preview",
    showBackground = false,
)
@Composable
fun BottomNavItemPreviewDark() {

    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = true) {

        BottomNavItem(
            title = "title",
            selected = true,
            onClick = {},
        )
    }
}

@Preview(
    name = "Dialog Preview",
    showBackground = false,
)
@Composable
fun BottomNavigationBarPreview() {

    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = false) {
        // ✅ مستقیماً CustomDialog را صدا بزن
        BottomNavigationBar(
            selectedTab = BottomTab.CATEGORY,
            actions = BottomBarActions(),
        )
    }
}

@Preview(
    name = "Dialog Preview",
    showBackground = false,
)
@Composable
fun BottomNavigationBarPreviewDark() {

    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = true) {
        // ✅ مستقیماً CustomDialog را صدا بزن
        BottomNavigationBar(
            selectedTab = BottomTab.CATEGORY,
            actions = BottomBarActions(),
        )
    }
}