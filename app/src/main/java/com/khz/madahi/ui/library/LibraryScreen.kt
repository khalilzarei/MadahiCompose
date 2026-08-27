// ui/library/LibraryScreen.kt
package com.khz.madahi.ui.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.khz.madahi.models.Category
import com.khz.madahi.ui.common.BottomBarActions
import com.khz.madahi.ui.common.BottomTab
import com.khz.madahi.ui.common.ErrorContentScreen
import com.khz.madahi.ui.components.BaseScreen
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.components.GlassTextField
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary

// ============================================================
// صفحه کتابچه — لیست دسته‌ها (جستجو + صفحه‌بندی + اسکرول)
// ============================================================

@Composable
fun LibraryScreen(
    bottomBarActions: BottomBarActions,
    onNavigateBack: () -> Unit,
    onCategoryClick: (Int) -> Unit
) {
    val context = LocalContext.current
    val colors = LocalMadahiColors.current

    val viewModel: LibraryViewModel = viewModel(factory = LibraryViewModelFactory())

    val uiState by viewModel.uiState.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val query by viewModel.query.collectAsState()
    val hasMore by viewModel.hasMore.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()

    val listState = rememberLazyListState()

    // لود بیشتر وقتی به نزدیک انتهای لیست رسیدیم
    LaunchedEffect(listState) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                    ?: 0
        }.collect { lastIndex ->
            if (lastIndex >= categories.size - 3 && hasMore && !isLoadingMore) {
                viewModel.loadMore()
            }
        }
    }

    BaseScreen(
        bottomBarActions = bottomBarActions,
        title = "کتابچه",
        subtitle = "📚",
        selectedBottomTab = BottomTab.LIBRARY,
        onHeaderBottonClicked = { onNavigateBack() },
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // ============ نوار جستجو ============
            GlassTextField(
                value = query,
                onValueChange = { viewModel.onQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 24.dp,
                        vertical = 12.dp
                    ),
                minHeight = 52.dp,
                hint = "جستجو در دسته‌ها..."
            )

            when (uiState) {

                is LibraryViewModel.UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                is LibraryViewModel.UiState.Error   -> {
                    ErrorContentScreen(
                        message = (uiState as LibraryViewModel.UiState.Error).message,
                        onRetry = { viewModel.loadPage(1) })
                }

                is LibraryViewModel.UiState.Success -> {
                    if (categories.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "🔍",
                                    fontSize = 44.sp
                                )
                                Spacer(Modifier.height(10.dp))
                                Text(
                                    text = if (query.isNotBlank()) "دسته‌ای با این نام پیدا نشد"
                                    else "دسته‌ای در کتابچه نیست",
                                    color = colors.textMuted,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 24.dp),
                            state = listState,
                            contentPadding = PaddingValues(bottom = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(
                                categories,
                                key = { it.id }) { category ->
                                LibraryCategoryItem(
                                    category = category,
                                    onClick = { onCategoryClick(category.id) })
                            }
                            // نشانگر بارگذاری صفحه بعد
                            if (isLoadingMore) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(22.dp),
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ============================================================
// آیتم دسته در کتابچه
// ============================================================

@Composable
private fun LibraryCategoryItem(
    category: Category,
    onClick: () -> Unit
) {
    val colors = LocalMadahiColors.current

    GlassCard3D(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = category.title,
                    color = colors.textPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (category.description.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = category.description,
                        color = colors.textMuted,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // شمارش اشعار (در صورت موجود بودن)
            category.poemCount?.let { count ->
                if (count > 0) {
                    Text(
                        text = "$count شعر",
                        color = colors.gold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }

            Text(
                text = "‹",
                color = colors.gold,
                fontSize = 22.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LibraryScreenPreview() {
    MadahiThemeGreen(darkTheme = true) {
        LibraryScreen(
            bottomBarActions = BottomBarActions(),
            onNavigateBack = {},
            onCategoryClick = {})
    }
}
