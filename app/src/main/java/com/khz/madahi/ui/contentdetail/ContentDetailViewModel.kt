// ui/contentdetail/ContentDetailViewModel.kt
package com.khz.madahi.ui.contentdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.repository.ContentRepository
import com.khz.madahi.helper.extention.logD
import com.khz.madahi.helper.extention.logE
import com.khz.madahi.models.Content
import com.khz.madahi.models.Favorite
import com.khz.madahi.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContentDetailViewModel(
    private val preferencesManager: PreferencesManager,
    private val contentRepository: ContentRepository,
    private val appDatabase: AppDatabase
) : ViewModel() {

    // ============ State ============
    private val _favoriteState = MutableStateFlow<FavoriteState>(FavoriteState.Loading)
    val favoriteState: StateFlow<FavoriteState> = _favoriteState.asStateFlow()

    private val _favorite = MutableStateFlow<Favorite?>(null)
    val favorite: StateFlow<Favorite?> = _favorite.asStateFlow()


    fun loadFavoriteStatus(content: Content) {
        viewModelScope.launch {
            logD("loadFavoriteStatus: contentId=${content.id}")

            val userId = preferencesManager.user?.id
                    ?: "0"
            logD("loadFavoriteStatus: userId=$userId")

            // ✅ بررسی در دیتابیس محلی
            val localFavorite = try {
                appDatabase.favoriteDAO()
                    .getFavorite(
                        content.id,
                        userId
                    )
            } catch (e: Exception) {
                logD(
                    "loadFavoriteStatus: error getting from DB $e"
                )
                null
            }

            logD("loadFavoriteStatus: localFavorite=$localFavorite")

            if (localFavorite != null) {
                _favorite.value = localFavorite
                _favoriteState.value = FavoriteState.Favorite
                logD("loadFavoriteStatus: ✅ Favorite (from local DB)")
            } else {
                // ✅ اگر در دیتابیس محلی نبود، از سرور سوال کن
                _favorite.value = null
                _favoriteState.value = FavoriteState.NotFavorite
                logD("loadFavoriteStatus: ❌ Not Favorite (local)")

                // ✅ همگام‌سازی با سرور در پس‌زمینه
                syncFavoriteStatus(content)
            }
        }
    }

    // ============ همگام‌سازی با سرور ============
    private fun syncFavoriteStatus(content: Content) {
        viewModelScope.launch {
            try {
                val userId = preferencesManager.user?.id
                        ?: "0"

                // ✅ دریافت لیست علاقه‌مندی‌ها از سرور
//                when (val result = contentRepository.getUserFavorites(userId)) {
//                    is Result.Success -> {
//                        val favorites = result.data
//                        logD("syncFavoriteStatus: favorites from server size=${favorites?.size}")
//
//                        // ✅ بررسی اینکه آیا این محتوا در لیست سرور وجود دارد
//                        val existsInServer = favorites?.any { it.contentId == content.id } == true
//
//                        if (existsInServer) {
//                            // ✅ اگر در سرور وجود دارد ولی در دیتابیس محلی نیست، آن را اضافه کن
//                            val favorite = Favorite(
//                                idFavorite = 0,
//                                contentId = content.id,
//                                id = System.currentTimeMillis()
//                                    .toString(),
//                                userId = userId,
//                                createdAt = null,
//                                updateAt = null
//                            )
//                            appDatabase.favoriteDAO()
//                                .insert(favorite)
//                            _favorite.value = favorite
//                            _favoriteState.value = FavoriteState.Favorite
//                            logD("syncFavoriteStatus: ✅ Favorite added to local DB")
//                        } else {
//                            _favoriteState.value = FavoriteState.NotFavorite
//                            logD("syncFavoriteStatus: ❌ Not Favorite on server")
//                        }
//                    }
//
//                    is Result.Error   -> {
//                        logD("syncFavoriteStatus: ${result.message}")
//                    }
//
//                    is Result.Loading -> { /* ignore */
//                    }
//                }
            } catch (e: Exception) {
                logD("syncFavoriteStatus: error $e")
            }
        }
    }

    // ============ Add to Favorites ============
    fun addToFavorites(
        content: Content,
        onComplete: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            logD("addToFavorites: contentId=${content.id}")

            val userId = preferencesManager.user?.id
                    ?: "0"

            // ✅ بررسی کن که قبلاً در دیتابیس وجود ندارد
            val existingFavorite = appDatabase.favoriteDAO()
                .getFavorite(
                    content.id,
                    userId
                )
            if (existingFavorite != null) {
                logD("addToFavorites: already exists in local DB")
                _favorite.value = existingFavorite
                _favoriteState.value = FavoriteState.Favorite
                onComplete?.invoke()
                return@launch
            }

            _favoriteState.value = FavoriteState.Loading

            when (val result = contentRepository.addFavorite(
                userId,
                content.id
            )) {
                is Result.Success -> {
                    val favorite = result.data

                    // ✅ بررسی کن که قبل از insert وجود ندارد
                    val checkExist = appDatabase.favoriteDAO()
                        .getFavorite(
                            content.id,
                            userId
                        )
                    if (checkExist == null) {
                        appDatabase.favoriteDAO()
                            .insert(favorite)
                        logD("addToFavorites: ✅ Favorite added to DB")
                    } else {
                        logD("addToFavorites: already exists, skipping insert")
                    }

                    _favorite.value = favorite
                    _favoriteState.value = FavoriteState.Favorite
                    onComplete?.invoke()
                }

                is Result.Error   -> {
                    // ✅ اگر خطا آمد و پیام حاوی "وجود دارد" بود
                    if (result.message.contains("وجود دارد") || result.message.contains("تکراری")) {
                        // یک Favorite محلی بساز
                        val newFavorite = Favorite(
                            idFavorite = 0,
                            contentId = content.id,
                            id = System.currentTimeMillis()
                                .toString(),
                            userId = userId,
                            createdAt = null,
                            updateAt = null
                        )

                        val checkExist = appDatabase.favoriteDAO()
                            .getFavorite(
                                content.id,
                                userId
                            )
                        if (checkExist == null) {
                            appDatabase.favoriteDAO()
                                .insert(newFavorite)
                            logD("addToFavorites: ✅ Favorite added to local DB")
                        } else {
                            logD("addToFavorites: already exists, skipping insert")
                        }

                        _favorite.value = newFavorite
                        _favoriteState.value = FavoriteState.Favorite
                        onComplete?.invoke()
                    } else {
                        _favoriteState.value = FavoriteState.Error(result.message)
                        logE("addToFavorites: ${result.message}")
                    }
                }

                is Result.Loading -> { /* ignore */
                }
            }
        }
    }

    // ============ Remove from Favorites ============
    fun removeFromFavorites(
        content: Content,
        onComplete: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            logD("removeFromFavorites: contentId=${content.id}")

            val userId = preferencesManager.user?.id
                    ?: "0"

            _favoriteState.value = FavoriteState.Loading

            // ✅ از repository حذف کن (هم سرور و هم دیتابیس محلی)
            when (val result = contentRepository.removeFavorite(
                userId,
                content.id
            )) {
                is Result.Success -> {
                    // ✅ حذف از State
                    _favorite.value = null
                    _favoriteState.value = FavoriteState.NotFavorite
                    logD("removeFromFavorites: ✅ Favorite removed")
                    onComplete?.invoke()
                }

                is Result.Error   -> {
                    // ✅ اگر خطا آمد، دوباره به دیتابیس اضافه کن
                    val existingFavorite = appDatabase.favoriteDAO()
                        .getFavorite(
                            content.id,
                            userId
                        )
                    if (existingFavorite != null) {
                        appDatabase.favoriteDAO()
                            .insert(existingFavorite)
                        _favorite.value = existingFavorite
                        _favoriteState.value = FavoriteState.Favorite
                    }
                    _favoriteState.value = FavoriteState.Error(result.message)
                    logD("removeFromFavorites: ${result.message}")
                }

                is Result.Loading -> { /* ignore */
                }
            }
        }
    }

    // ui/contentdetail/ContentDetailViewModel.kt

    // ============ Toggle Favorite ============
    fun toggleFavorite(
        content: Content,
        onComplete: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            logD("toggleFavorite: contentId=${content.id}")

            val userId = preferencesManager.user?.id
                    ?: "0"

            // ✅ وضعیت را به Loading ببر
            _favoriteState.value = FavoriteState.Loading

            when (val result = contentRepository.toggleFavorite(
                userId,
                content.id
            )) {
                is Result.Success -> {
                    val favorite = result.data

                    if (favorite == null) {
                        // ✅ حذف شده
                        _favorite.value = null
                        _favoriteState.value = FavoriteState.NotFavorite
                        logD("toggleFavorite: ✅ Removed from favorites")
                    } else {
                        // ✅ اضافه شده
                        _favorite.value = favorite
                        _favoriteState.value = FavoriteState.Favorite
                        logD("toggleFavorite: ✅ Added to favorites")
                    }

                    onComplete?.invoke()
                }

                is Result.Error   -> {
                    _favoriteState.value = FavoriteState.Error(result.message)
                    logD("toggleFavorite: ${result.message}")

                    // ✅ در صورت خطا، وضعیت قبلی را بازیابی کن
                    val userId = preferencesManager.user?.id
                            ?: "0"
                    val existingFavorite = appDatabase.favoriteDAO()
                        .getFavorite(
                            content.id,
                            userId
                        )
                    if (existingFavorite != null) {
                        _favorite.value = existingFavorite
                        _favoriteState.value = FavoriteState.Favorite
                    } else {
                        _favoriteState.value = FavoriteState.NotFavorite
                    }
                }

                is Result.Loading -> { /* ignore */
                }
            }
        }
    }
}

// ============ Favorite State ============
sealed class FavoriteState {
    object Loading : FavoriteState()
    object Favorite : FavoriteState()
    object NotFavorite : FavoriteState()
    data class Error(val message: String) : FavoriteState()
}