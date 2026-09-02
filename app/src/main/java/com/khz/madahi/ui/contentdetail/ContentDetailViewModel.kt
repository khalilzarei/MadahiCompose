// ui/contentdetail/ContentDetailViewModel.kt
package com.khz.madahi.ui.contentdetail

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.repository.ContentRepository
import com.khz.madahi.data.remote.repository.PremiumRepository
import com.khz.madahi.helper.GUEST_USER_ID
import com.khz.madahi.helper.extention.logD
import com.khz.madahi.helper.extention.logE
import com.khz.madahi.models.Content
import com.khz.madahi.models.Favorite
import com.khz.madahi.ui.poems.PremiumUiState
import com.khz.madahi.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContentDetailViewModel(
    private val preferencesManager: PreferencesManager,
    private val contentRepository: ContentRepository,
    private val appDatabase: AppDatabase,
    private val premiumRepository: PremiumRepository
) : ViewModel() {

    // ============ State ============
    private val _favoriteState = MutableStateFlow<FavoriteState>(FavoriteState.Loading)
    val favoriteState: StateFlow<FavoriteState> = _favoriteState.asStateFlow()

    private val _favorite = MutableStateFlow<Favorite?>(null)
    val favorite: StateFlow<Favorite?> = _favorite.asStateFlow()

    // وضعیت نسخه پرو (null = هنوز چک نشده)
    private val _premiumState = MutableStateFlow<PremiumUiState?>(null)
    val premiumState: StateFlow<PremiumUiState?> = _premiumState.asStateFlow()

    // وضعیت آپلود ویس
    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> = _uploadState.asStateFlow()

    // پیام بررسی لینک فایل بعد از آپلود (تشخیص مشکل لینک در مقابل مشکل فایل)
    private val _audioUrlMessage = MutableStateFlow<String?>(null)
    val audioUrlMessage: StateFlow<String?> = _audioUrlMessage.asStateFlow()

    fun loadFavoriteStatus(content: Content) {
        viewModelScope.launch {
            logD("loadFavoriteStatus: contentId=${content.id}")

            val userId = preferencesManager.user?.id
                    ?: GUEST_USER_ID
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
                    ?: GUEST_USER_ID

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
                                .toInt(),
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
                    ?: GUEST_USER_ID

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

    // ============ Toggle Favorite ============
    fun toggleFavorite(
        content: Content,
        onComplete: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            logD("toggleFavorite: contentId=${content.id}")

            val userId = preferencesManager.user?.id
                    ?: GUEST_USER_ID

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

    // ============ Delete Content ============
    fun deleteContent(
        content: Content,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            logD("deleteContent: contentId=${content.id}")

            val userId = preferencesManager.user?.id
                    ?: GUEST_USER_ID

            // ✅ ابتدا حذف از دیتابیس محلی
            appDatabase.contentDAO()
                .delete(content)

            when (val result = contentRepository.deleteContent(
                userId,
                content.id
            )) {
                is Result.Success -> {
                    logD("deleteContent: ✅ Deleted from server")
                    onSuccess()
                }

                is Result.Error   -> {
                    // ✅ در صورت خطا، حذف محلی را برگشت می‌دهیم
                    appDatabase.contentDAO()
                        .insert(content)
                    onError(result.message)
                }

                is Result.Loading -> { /* ignore */
                }
            }
        }
    }

    // ============ چک نسخه پرو (برای گیت کردن افزودن ویس) ============

    /**
     * وضعیت پرو را برمی‌گرداند؛ اگر قبلاً چک نشده، از سرور می‌گیرد.
     * در صورت خطای ارتباط، true برمی‌گرداند (سرور در نهایت گیت نهایی است).
     */
    fun ensurePremium(onResult: (Boolean) -> Unit) {
        val cached = _premiumState.value
        if (cached is PremiumUiState.Pro) {
            onResult(true)
            return
        }
        if (cached is PremiumUiState.Guest) {
            onResult(false)
            return
        }

        viewModelScope.launch {
            val userId = preferencesManager.user?.id
                    ?: GUEST_USER_ID

            when (val result = premiumRepository.getPremiumStatus(userId)) {
                is Result.Success -> {
                    val isPro = result.data.isPremium
                    _premiumState.value = if (isPro) {
                        PremiumUiState.Pro
                    } else {
                        PremiumUiState.Guest
                    }
                    onResult(isPro)
                }

                is Result.Error   -> {
                    logD("ensurePremium: error ${result.message} — allow try")
                    onResult(true)
                }

                is Result.Loading -> { /* ignore */
                }
            }
        }
    }

    // ============ آپلود ویس/سبک (نسخه پرو) ============
    // startSec/durationSec: محدوده‌ی کات — برش توسط سرور انجام می‌شود (-1 = بدون برش)
    fun uploadAudio(
        context: Context,
        content: Content,
        uri: Uri,
        startSec: Double = -1.0,
        durationSec: Double = -1.0
    ) {
        viewModelScope.launch {
            _uploadState.value = UploadState.Uploading
            _audioUrlMessage.value = null

            val userId = preferencesManager.user?.id
                    ?: GUEST_USER_ID

            when (val result = contentRepository.uploadAudio(
                context,
                userId,
                content,
                uri,
                startSec,
                durationSec
            )) {
                is Result.Success -> {
                    // ✅ به‌روزرسانی فقط audio_url در دیتابیس محلی
                    // (متد update قبلی کار نمی‌کرد چون result.data.idContent = 0 بود
                    //  و Room با primary key idContent ردیف پیدا نمی‌کرد)
                    try {
                        val audioUrl = result.data.audioUrl
                        appDatabase.contentDAO()
                            .updateAudioUrl(content.id, audioUrl)
                        logD("uploadAudio: local DB updated via updateAudioUrl, audioUrl=$audioUrl")
                    } catch (e: Exception) {
                        logE("uploadAudio: local update error $e")
                    }
                    _uploadState.value = UploadState.Success(audioUrl = result.data.audioUrl)
                    logD("uploadAudio: ✅ contentId=${content.id}, audioUrl=${result.data.audioUrl}")

                    // ✅ بررسی اینکه لینک برگشتی واقعاً کار می‌کند
                    val url = result.data.audioUrl
                    if (!url.isNullOrBlank()) {
                        _audioUrlMessage.value = "در حال بررسی لینک فایل…"
                        when (val check = contentRepository.verifyAudioUrl(url)) {
                            is Result.Success -> {
                                _audioUrlMessage.value = "✅ لینک فایل سالم است (${check.data})"
                            }

                            is Result.Error   -> {
                                _audioUrlMessage.value = "⚠️ آپلود شد ولی لینک ایراد دارد: ${check.message}"
                            }

                            is Result.Loading -> { /* ignore */
                            }
                        }
                    }
                }

                is Result.Error   -> {
                    _uploadState.value = UploadState.Error(result.message)
                }

                is Result.Loading -> { /* ignore */
                }
            }
        }
    }

    fun clearUploadState() {
        _uploadState.value = UploadState.Idle
    }
}

// ============ States ============

sealed class UploadState {
    object Idle : UploadState()
    object Uploading : UploadState()
    data class Success(val audioUrl: String? = null) : UploadState()
    data class Error(val message: String) : UploadState()
}

// ============ Favorite State ============
sealed class FavoriteState {
    object Loading : FavoriteState()
    object Favorite : FavoriteState()
    object NotFavorite : FavoriteState()
    data class Error(val message: String) : FavoriteState()
}