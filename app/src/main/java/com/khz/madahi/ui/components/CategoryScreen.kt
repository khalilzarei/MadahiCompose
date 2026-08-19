package com.khz.madahi.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khz.madahi.ui.common.BottomBarActions
import com.khz.madahi.ui.common.BottomTab
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen

@Composable
fun CategoryScreen(
    categories: List<String>,
    onCategoryClick: (String) -> Unit,
    onAddCategory: () -> Unit,
    onSettings: () -> Unit,
    onFavorites: () -> Unit
) {
    val colors = LocalMadahiColors.current

    MadahiBackground {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            TopHeader(
                title = "دفترچه مداحی",
                subtitle = "دسته‌بندی اشعار",
                onHeaderBottonClicked = onSettings
            )

            Spacer(Modifier.height(20.dp))

            GlassSearchBar()

            Spacer(Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

//                Text(
//                    text = "دسته‌بندی‌ها",
//                    color = colors.textPrimary,
//                    fontSize = 21.sp,
//                    fontWeight = FontWeight.Bold
//                )

                Gold3DButton(
                    text = "+ دسته‌بندی",
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp),
                    onClick = onAddCategory
                )

                ThreeDButton(
                    text = "+ دسته‌بندی",
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp),
                    onClick = onAddCategory
                )

                Delete3DButton(
                    text = "حذف",
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp),
                    onClick = onAddCategory
                )
            }

            Spacer(Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(categories) { category ->

                    CategoryItem(
                        title = category,
                        onClick = {
                            onCategoryClick(category)
                        })
                }
            }

            BottomNavigationBar(
                selectedTab = BottomTab.CATEGORY,
                actions = BottomBarActions(),
            )
        }
    }
}

@Preview(
    name = "Dialog Preview",
    showBackground = true,
)
@Composable
fun CategoryScreenPreview() {

    val colors = LocalMadahiColors.current
    MadahiThemeGreen(darkTheme = true) {
        // ✅ مستقیماً CustomDialog را صدا بزن
        CategoryScreen(
            categories = arrayListOf(
                "one",
                "two",
                "three",
                "one",
                "two",
                "three",
            ),
            onCategoryClick = {},
            onAddCategory = {},
            onSettings = {},
            onFavorites = {},
        )
    }
}




