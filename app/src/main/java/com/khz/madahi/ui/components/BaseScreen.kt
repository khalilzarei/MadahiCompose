package com.khz.madahi.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khz.madahi.ui.common.BottomBarActions
import com.khz.madahi.ui.common.BottomBarView
import com.khz.madahi.ui.common.BottomTab
import com.khz.madahi.ui.theme.MadahiTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    bottomBarActions: BottomBarActions,
    title: String,
    subtitle: String = "",
    onHeaderBottonClicked: () -> Unit = {},
    onFabAddBottonClicked: () -> Unit = {},
    selectedBottomTab: BottomTab,
    isCategory: Boolean = false,
    content: @Composable () -> Unit
) {
    MadahiBackground {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            TopTitleBar(
                title = title,
                subTitle = subtitle,
                onBack = onHeaderBottonClicked,
                isCategory = isCategory
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                content()
            }
            BottomBarView(
                selectedTab = selectedBottomTab,
                actions = bottomBarActions,
                onFabAddBottonClicked = onFabAddBottonClicked,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BaseScreenPreview() {
    MadahiTheme(darkTheme = true) {
        BaseScreen(
            bottomBarActions = BottomBarActions(),
            title = "عنوان",
            subtitle = "",
            selectedBottomTab = BottomTab.ABOUT,
            onHeaderBottonClicked = {},
            onFabAddBottonClicked = {},
        ) {
            Column {
                Text("Test")
            }
        }
    }
}