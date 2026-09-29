// ui/favorite/FavoritesScreen.kt
package com.khz.madahi.ui.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.api.RetrofitClient
import com.khz.madahi.data.remote.repository.ContentRepository
import com.khz.madahi.models.Content
import com.khz.madahi.ui.common.BottomBarActions
import com.khz.madahi.ui.common.BottomTab
import com.khz.madahi.ui.components.BaseScreen
import com.khz.madahi.ui.content.ContentItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    bottomBarActions: BottomBarActions,
    onNavigateToContentDetail: (Content) -> Unit,
) {
    val context = LocalContext.current

    val viewModel: FavoritesViewModel = viewModel(
        factory = FavoritesViewModelFactory(
            preferencesManager = PreferencesManager(context),
            contentRepository = ContentRepository(
                apiService = RetrofitClient.apiService,
                contentDao = AppDatabase.getInstance(context)
                    .contentDAO(),
                favoriteDao = AppDatabase.getInstance(context)
                    .favoriteDAO()
            ),
            appDatabase = AppDatabase.getInstance(context)
        )
    )

    val uiState by viewModel.uiState.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    // ✅ وقتی صفحه به foreground می‌آید، لیست را رفرش کن
    DisposableEffect(Unit) {
        onDispose {
            // وقتی صفحه بسته می‌شود
        }
    }

    // ✅ استفاده از LaunchedEffect برای رفرش هنگام بازگشت
    // این روش با NavHost و popBackStack کار می‌کند
    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }

    // با استفاده از Lifecycle را مشاهده کنید
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                // ✅ وقتی صفحه دوباره قابل مشاهده می‌شود، لیست را رفرش کن
                viewModel.loadFavorites()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    BaseScreen(
        bottomBarActions = bottomBarActions,
        title = "علاقه مندی ها",
        selectedBottomTab = BottomTab.FAVORITES,
        isCategory = true,
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when (uiState) {
                is FavoritesUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "در حال بارگذاری برگزیده‌ها...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                is FavoritesUiState.Success -> {
                    if (favorites.isEmpty()) {
                        // ============ صفحه خالی ============
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,
                                    modifier = Modifier.size(80.dp),
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                                )
                                Text(
                                    text = "هیچ برگزیده‌ای وجود ندارد",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = "با کلیک روی ❤️ در صفحه محتوا، آن را به برگزیده‌ها اضافه کنید",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                    modifier = Modifier.padding(horizontal = 32.dp)
                                )
                            }
                        }
                    } else {
                        // ============ لیست برگزیده‌ها ============
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                horizontal = 12.dp,
                                vertical = 8.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                items = favorites,
                                key = { "${it.id}_${it.idContent}" }) { content ->
                                ContentItem(
                                    content = content,
                                    onClick = { onNavigateToContentDetail(content) },
                                    onEditClick = {},
                                    onDeleteClick = {})
                            }
                        }
                    }
                }

                is FavoritesUiState.Error   -> {
                    // ============ صفحه خطا ============
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                modifier = Modifier.size(80.dp),
                                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                            )
                            Text(
                                text = (uiState as FavoritesUiState.Error).message,
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 32.dp)
                            )
                            Button(
                                onClick = viewModel::loadFavorites,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("تلاش مجدد")
                            }
                        }
                    }
                }
            }
        }
    }
}

//// ============ Preview ============
//@Preview(showBackground = true)
//@Composable
//fun FavoritesScreenPreview() {
//    MaterialTheme {
//        FavoritesScreen(
//            onNavigateBack = {},
//            onNavigateToContentDetail = {})
//    }
//}