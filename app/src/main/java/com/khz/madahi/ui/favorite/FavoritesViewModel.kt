// ui/favorite/FavoritesViewModel.kt
package com.khz.madahi.ui.favorite

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.repository.ContentRepository
import com.khz.madahi.helper.GUEST_USER_ID
import com.khz.madahi.models.Content
import com.khz.madahi.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val preferencesManager: PreferencesManager,
    private val contentRepository: ContentRepository,
    private val appDatabase: AppDatabase
) : ViewModel() {

    companion object {
        private const val TAG = "FavoritesVM"
    }

    // ============ State ============
    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Loading)
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    private val _favorites = MutableStateFlow<List<Content>>(emptyList())
    val favorites: StateFlow<List<Content>> = _favorites.asStateFlow()

    // ============ Init ============
    init {
        loadFavorites()
    }

    // ============ Load Favorites ============
    // ui/favorite/FavoritesViewModel.kt

    private var isLoading = false

    fun loadFavorites() {
        if (isLoading) {
            Log.d(
                TAG,
                "loadFavorites: already loading, skipping"
            )
            return
        }

        viewModelScope.launch {
            isLoading = true
            _uiState.value = FavoritesUiState.Loading
            Log.d(
                TAG,
                "loadFavorites: loading..."
            )

            try {
                // ✅ 1) نمایش سریع از دیتابیس محلی (کش)
                val localFavorites = appDatabase.contentDAO()
                    .getFavorites()
                val distinctLocal = localFavorites.distinctBy { it.id }
                Log.d(
                    TAG,
                    "loadFavorites: local favorites size=${distinctLocal.size}"
                )

                if (distinctLocal.isNotEmpty()) {
                    _favorites.value = distinctLocal
                    _uiState.value = FavoritesUiState.Success
                }

                // ✅ 2) همگام‌سازی با سرور — لیست اصلی از سرور می‌آید
                // (حتی اگر دیتابیس محلی پاک شده باشد، لیست درست نمایش داده می‌شود)
                val userId = preferencesManager.user?.id
                        ?: GUEST_USER_ID

                when (val result = contentRepository.getFavorites(userId)) {
                    is Result.Success -> {
                        _favorites.value = result.data.distinctBy { it.id }
                        _uiState.value = FavoritesUiState.Success
                        Log.d(
                            TAG,
                            "loadFavorites: server favorites size=${result.data.size}"
                        )
                    }

                    is Result.Error   -> {
                        // اگر کش خالی بود خطا نشان بده؛
                        // اگر کش داشت، لیست محلی دست‌نخورده می‌ماند
                        if (_favorites.value.isEmpty()) {
                            _uiState.value = FavoritesUiState.Error(result.message)
                        }
                    }

                    is Result.Loading -> { /* ignore */
                    }
                }

            } catch (e: Exception) {
                Log.e(
                    TAG,
                    "loadFavorites: error",
                    e
                )
                if (_favorites.value.isEmpty()) {
                    _uiState.value = FavoritesUiState.Error("خطا در بارگذاری برگزیده‌ها")
                }
            } finally {
                isLoading = false
            }
        }
    }

    // ============ Refresh ============
    fun refresh() {
        loadFavorites()
    }
}

// ============ UI State ============
sealed class FavoritesUiState {
    object Loading : FavoritesUiState()
    object Success : FavoritesUiState()
    data class Error(val message: String) : FavoritesUiState()
}