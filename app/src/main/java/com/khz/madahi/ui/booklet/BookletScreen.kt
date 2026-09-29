// ui/booklet/BookletScreen.kt
package com.khz.madahi.ui.booklet

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.components.GlassTextField
import com.khz.madahi.ui.components.MadahiBackground
import com.khz.madahi.ui.components.TopTitleBar
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary

// ============================================================
// صفحه اصلی کتابچه — طراحی هماهنگ با دفترچه + بارگذاری تدریجی
// ------------------------------------------------------------
// - هدر TopTitleBar (مثل دفترچه)
// - جستجو در دسته‌ها
// - لیست دسته‌ها: اول ۳۰ تا، با رسیدن به انتها ۳۰ تای بعدی
// ============================================================

@Composable
fun BookletScreen(
    onNavigateBack: () -> Unit,
    onNavigateHome: () -> Unit = onNavigateBack,
    onNavigateToSection: (Int, String) -> Unit
) {
    val viewModel: BookletViewModel = viewModel(
        factory = BookletViewModelFactory()
    )

    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val listState = rememberLazyListState()

    val colors = LocalMadahiColors.current

    // ============ تشخیص رسیدن به انتهای لیست ============
    val shouldLoadMore by remember {
        derivedStateOf {
            val info = listState.layoutInfo
            val lastVisible = info.visibleItemsInfo.lastOrNull()?.index
                    ?: -1
            info.totalItemsCount > 0 && lastVisible >= info.totalItemsCount - 5
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            viewModel.loadMore()
        }
    }

    MadahiBackground {
        Column(modifier = Modifier.fillMaxSize()) {

            // ============ هدر (مثل دفترچه) ============
            TopTitleBar(
                title = "کتابچه",
                subTitle = "اشعار و مراثی",
                onBack = onNavigateBack,
                isCategory = true,
                onHome = onNavigateHome
            )

            // ============ جستجو ============
            GlassTextField(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                minHeight = 54.dp,
                hint = "جستجو در دسته‌بندی‌ها...",
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(12.dp))

            // ============ محتوا (فضای باقی‌مانده) ============
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (val state = uiState) {
                    is BookletUiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = colors.gold)
                        }
                    }

                    is BookletUiState.Success -> {
                        if (state.sections.isEmpty()) {
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
                                        text = "موردی یافت نشد",
                                        color = colors.textMuted,
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        } else {
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(
                                    horizontal = 24.dp,
                                    vertical = 8.dp
                                ),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(
                                    items = state.sections,
                                    key = { it.id }) { section ->
                                    BookletSectionCard(
                                        section = section,
                                        onClick = {
                                            onNavigateToSection(
                                                section.id,
                                                section.title
                                            )
                                        })
                                }

                                // ============ لودینگ صفحه بعد ============
                                if (state.isLoadingMore) {
                                    item(key = "loading_more") {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(
                                                color = colors.gold,
                                                modifier = Modifier.size(28.dp),
                                                strokeWidth = 3.dp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    is BookletUiState.Error   -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "⚠️ ${state.message}",
                                    color = colors.textMuted,
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(Modifier.height(16.dp))
                                GlassCard3D(modifier = Modifier
                                    .clickable { viewModel.loadSections() }
                                    .padding(horizontal = 32.dp)) {
                                    Text(
                                        text = "تلاش مجدد",
                                        color = colors.gold,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(
                                            horizontal = 28.dp,
                                            vertical = 14.dp
                                        )
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
// کارت هر دسته کتابچه — هماهنگ با CategoryItem دفترچه
// ============================================================

@Composable
private fun BookletSectionCard(
    section: BookletSection,
    onClick: () -> Unit
) {
    val colors = LocalMadahiColors.current

    GlassCard3D(
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp)
            .clickable { onClick() }) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // عنوان + تعداد شعر
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = section.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.textPrimary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "${section.itemCount} شعر",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.gold,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
    }
}
