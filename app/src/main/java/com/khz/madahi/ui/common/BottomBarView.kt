// ui/common/BottomBarView.kt

package com.khz.madahi.ui.common

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.goldLight
import com.khz.madahi.ui.theme.primaryDark
import com.khz.madahi.ui.theme.primaryLight
import com.khz.madahi.ui.theme.surfaceGlass
import com.khz.madahi.ui.theme.surfaceGlassLight
import com.khz.madahi.ui.theme.textPrimary

// ============================================================
// تب‌های پایین
// ============================================================

enum class BottomTab {
    MESSAGE,
    PROFILE,
    CATEGORY,
    FAVORITES,
    SETTINGS,
    ABOUT,
    POEMS     // 🎧 صفحه شعر و سبک (در نوار پایین نمایش نمی‌گیرد؛ فقط برای هایلایت)
}

// ============================================================
// اکشن‌های BottomBar
// ============================================================

data class BottomBarActions(
    val onBackClick: () -> Unit = {},
    val onHomeClick: () -> Unit = {},
    val onCategoryClick: () -> Unit = {},
    val onFavoritesClick: () -> Unit = {},
    val onSettingsClick: () -> Unit = {},
    val onProfileClick: () -> Unit = {},
    val onAboutClick: () -> Unit = {},
    val onMessageClick: () -> Unit = {},
    val onPoemsClick: () -> Unit = {},
)

// ============================================================
// BottomBar
// ============================================================

@Composable
fun BottomBarView(
    selectedTab: BottomTab = BottomTab.CATEGORY,
    actions: BottomBarActions = BottomBarActions(),
    onFabAddBottonClicked: () -> Unit,
) {

    GlassCard3D(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .height(92.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 7.dp,
                    vertical = 7.dp
                ),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            BottomBarItem(
                icon = Icons.Default.GridView,
                label = "دسته‌بندی",
                selected = selectedTab == BottomTab.CATEGORY,
                onClick = actions.onCategoryClick,
                modifier = Modifier.weight(1f)
            )

            BottomBarItem(
                icon = if (selectedTab == BottomTab.FAVORITES) {
                    Icons.Default.Favorite
                } else {
                    Icons.Default.FavoriteBorder
                },
                label = "علاقه‌مندی‌ها",
                selected = selectedTab == BottomTab.FAVORITES,
                onClick = actions.onFavoritesClick,
                modifier = Modifier.weight(1f)
            )

            BottomBarFab {
                onFabAddBottonClicked()
            }

            BottomBarItem(
                icon = Icons.Default.Settings,
                label = "تنظیمات",
                selected = selectedTab == BottomTab.SETTINGS,
                onClick = actions.onSettingsClick,
                modifier = Modifier.weight(1f)
            )

            BottomBarItem(
                icon = Icons.AutoMirrored.Filled.Message,
                label = "پیام‌ها",
                selected = selectedTab == BottomTab.MESSAGE,
                onClick = actions.onMessageClick,
                modifier = Modifier.weight(1f)
            )

        }

    }
}

// ============================================================
// آیتم سه‌بعدی BottomBar
// ============================================================

@Composable
private fun BottomBarItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val colors = LocalMadahiColors.current

    val interactionSource = remember {
        MutableInteractionSource()
    }

    val isPressed by interactionSource.collectIsPressedAsState()

    val surfaceOffset by animateDpAsState(
        targetValue = if (isPressed) 3.dp else 0.dp,
        label = "bottom_item_offset"
    )

    val shadowElevation by animateDpAsState(
        targetValue = when {
            isPressed -> 3.dp
            selected  -> 8.dp
            else      -> 0.dp
        },
        label = "bottom_item_shadow"
    )

    val shape = RoundedCornerShape(17.dp)

    Box(
        modifier = modifier
            .height(70.dp)
            .padding(horizontal = 3.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center,

        ) {

        if (selected) {
            GlassCard3D(
                modifier = modifier
                    .fillMaxSize()
                    .shadow(
                        elevation = 6.dp,
                        shape = shape,
                        ambientColor = Color.Red,
                        spotColor = Color.Red,
                    )
            ) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = colors.textPrimary,
                        modifier = Modifier.size(25.dp)
                    )

                    Text(
                        text = label,
                        color = colors.textPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        } else {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = colors.textPrimary,
                    modifier = Modifier.size(25.dp)
                )

                Text(
                    text = label,
                    color = colors.textPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

// ============================================================
// FAB سه‌بعدی
// ============================================================

@Composable
private fun BottomBarFab(
    onClick: () -> Unit
) {
    val colors = LocalMadahiColors.current

    val interactionSource = remember {
        MutableInteractionSource()
    }

    val isPressed by interactionSource.collectIsPressedAsState()

    /*
     * هنگام لمس، سطح اصلی کمی پایین می‌رود.
     */
    val surfaceOffset by animateDpAsState(
        targetValue = if (isPressed) 4.dp else 0.dp,
        label = "fab_surface_offset"
    )

    /*
     * هنگام لمس، سایه کمتر می‌شود.
     */
    val shadowElevation by animateDpAsState(
        targetValue = if (isPressed) 5.dp else 16.dp,
        label = "fab_shadow"
    )

    val shape = CircleShape

    Box(
        modifier = Modifier.size(82.dp),
        contentAlignment = Alignment.Center
    ) {

        /*
         * =====================================================
         * لایه‌ی زیرین
         * =====================================================
         *
         * این لایه ضخامت سه‌بعدی FAB را ایجاد می‌کند.
         */

        Box(
            modifier = Modifier
                .size(72.dp)
                .offset(y = 7.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            colors.primaryDark,
                            colors.primaryDark.copy(alpha = 0.90f),
                            Color.Black.copy(alpha = 0.45f)
                        )
                    ),
                    shape = shape
                )
                .border(
                    width = 1.dp,
                    color = Color.Black.copy(alpha = 0.45f),
                    shape = shape
                )
        )

        /*
         * =====================================================
         * بدنه‌ی اصلی Glass 3D
         * =====================================================
         */

        Box(
            modifier = Modifier
                .size(72.dp)
                .offset(y = surfaceOffset)
                .shadow(
                    elevation = shadowElevation,
                    shape = shape,
                    ambientColor = Color.Black.copy(alpha = 0.70f),
                    spotColor = Color.Black.copy(alpha = 0.85f)
                )
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            colors.surfaceGlassLight.copy(alpha = 0.98f),
                            colors.surfaceGlass.copy(alpha = 0.96f),
                            colors.surfaceGlass.copy(alpha = 0.92f),
                            colors.primaryDark.copy(alpha = 0.72f)
                        )
                    ),
                    shape = shape
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.34f),
                            colors.primaryLight.copy(alpha = 0.24f),
                            colors.gold.copy(alpha = 0.20f),
                            Color.Transparent
                        )
                    ),
                    shape = shape
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {

            /*
             * =================================================
             * Highlight اصلی
             * =================================================
             */

            Box(
                modifier = Modifier
                    .size(68.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.14f),
                                Color.White.copy(alpha = 0.045f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )

            /*
             * =================================================
             * نور نقطه‌ای شیشه
             * =================================================
             */

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .offset(
                        x = (-11).dp,
                        y = (-14).dp
                    )
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.22f),
                                Color.White.copy(alpha = 0.07f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )

            /*
             * =================================================
             * حلقه‌ی طلایی داخلی
             * =================================================
             */

            Box(
                modifier = Modifier
                    .size(58.dp)
                    .border(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colors.goldLight.copy(alpha = 0.35f),
                                colors.gold.copy(alpha = 0.18f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )

            /*
             * =================================================
             * Inner Glass
             * =================================================
             */

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                colors.primaryLight.copy(alpha = 0.12f),
                                colors.primary.copy(alpha = 0.08f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.13f),
                        shape = CircleShape
                    )
            )

            /*
             * =================================================
             * آیکن
             * =================================================
             */

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "افزودن",
                tint = colors.gold,
                modifier = Modifier.size(34.dp)
            )
        }
    }
}

// ============================================================
// Preview
// ============================================================

@Preview()
@Composable
fun CategoryBottomBarPreview() {

    MadahiThemeGreen(
        darkTheme = false
    ) {

        BottomBarView(
            selectedTab = BottomTab.CATEGORY,
            actions = BottomBarActions(),
            onFabAddBottonClicked = {},
        )
    }
}

@Preview()
@Composable
fun CategoryBottomBarPreviewDark() {

    MadahiThemeGreen(
        darkTheme = true
    ) {

        BottomBarView(
            selectedTab = BottomTab.CATEGORY,
            actions = BottomBarActions(),
            onFabAddBottonClicked = {},
        )
    }
}