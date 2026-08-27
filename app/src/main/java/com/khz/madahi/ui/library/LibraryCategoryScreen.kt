// ui/library/LibraryCategoryScreen.kt
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
import com.khz.madahi.models.Content
import com.khz.madahi.ui.common.BottomBarActions
import com.khz.madahi.ui.common.BottomTab
import com.khz.madahi.ui.common.ErrorContentScreen
import com.khz.madahi.ui.components.BaseScreen
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary

// ============================================================
// صفحه اشعار یک دسته در کتابچه (اسکرول + صفحه‌بندی)
// ============================================================

@Composable
fun LibraryCategoryScreen(
    bottomBarActions: BottomBarActions,
    categoryId: Int,
    onNavigateBack: () -> Unit,
    onPoemClick: (Int) -> Unit
) {
    val context = LocalContext.current
    val colors = LocalMadahiColors.current

    val viewModel: LibraryCategoryViewModel = viewModel(
        factory = LibraryCategoryViewModelFactory(categoryId)
    )

    val uiState by viewModel.uiState.collectAsState()
    val poems by viewModel.poems.collectAsState()
    val hasMore by viewModel.hasMore.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()

    val listState = rememberLazyListState()

    // لود بیشتر وقتی به نزدیک انتهای لیست رسیدیم
    LaunchedEffect(listState) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                    ?: 0
        }.collect { lastIndex ->
                if (lastIndex >= poems.size - 3 && hasMore && !isLoadingMore) {
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
        when (uiState) {

            is LibraryCategoryViewModel.UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            is LibraryCategoryViewModel.UiState.Error   -> {
                ErrorContentScreen(
                    message = (uiState as LibraryCategoryViewModel.UiState.Error).message,
                    onRetry = { viewModel.loadPage(1) })
            }

            is LibraryCategoryViewModel.UiState.Success -> {
                if (poems.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "📜",
                                fontSize = 44.sp
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text = "این دسته هنوز شعری ندارد",
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
                            poems,
                            key = { it.id }) { poem ->
                            LibraryPoemItem(
                                poem = poem,
                                onClick = { onPoemClick(poem.id) })
                        }
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

// ============================================================
// آیتم شعر در کتابچه
// ============================================================

@Composable
private fun LibraryPoemItem(
    poem: Content,
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
                    text = poem.subject,
                    color = colors.textPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (poem.answer.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = poem.answer,
                        color = colors.textMuted,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
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
private fun LibraryCategoryScreenPreview() {
    MadahiThemeGreen(darkTheme = true) {
        LibraryCategoryScreen(
            bottomBarActions = BottomBarActions(),
            categoryId = 1,
            onNavigateBack = {},
            onPoemClick = {})
    }
}
