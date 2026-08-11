// ui/favorite/FavoritesViewModel.kt
package com.khz.madahi.ui.favorite

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.repository.ContentRepository
import com.khz.madahi.models.Content
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

            val userId = preferencesManager.user?.id
                    ?: "0"
            Log.d(
                TAG,
                "loadFavorites: userId=$userId"
            )

            try {
                // ✅ دریافت از دیتابیس محلی
                val localFavorites = appDatabase.contentDAO()
                    .getFavorites()
                Log.d(
                    TAG,
                    "loadFavorites: local favorites size=${localFavorites.size}"
                )

                // ✅ جلوگیری از دوبار اضافه شدن با استفاده از Distinct
                val distinctFavorites = localFavorites.distinctBy { it.id }

                if (distinctFavorites.isNotEmpty()) {
                    _favorites.value = distinctFavorites
                    _uiState.value = FavoritesUiState.Success
                    Log.d(
                        TAG,
                        "loadFavorites: loaded ${distinctFavorites.size} favorites"
                    )
                } else {
                    _favorites.value = emptyList()
                    _uiState.value = FavoritesUiState.Success
                }

                // همگام‌سازی با سرور (اختیاری)
                // ...

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