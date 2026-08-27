// ui/poems/PoemsViewModel.kt
package com.khz.madahi.ui.poems

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.repository.PremiumRepository
import com.khz.madahi.helper.GUEST_USER_ID
import com.khz.madahi.helper.extention.logD
import com.khz.madahi.models.Content
import com.khz.madahi.utils.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PoemsViewModel(
    private val preferencesManager: PreferencesManager,
    private val premiumRepository: PremiumRepository,
    private val appDatabase: AppDatabase
) : ViewModel() {

    // ============ State ============
    private val _uiState = MutableStateFlow<PoemsUiState>(PoemsUiState.Loading)
    val uiState: StateFlow<PoemsUiState> = _uiState.asStateFlow()

    private val _poems = MutableStateFlow<List<Content>>(emptyList())
    val poems: StateFlow<List<Content>> = _poems.asStateFlow()

    // وضعیت پخش (id محتوایی که در حال پخش است)
    private val _playingId = MutableStateFlow<Int?>(null)
    val playingId: StateFlow<Int?> = _playingId.asStateFlow()

    // وضعیت فاکتور زرین‌پال
    private val _invoiceState = MutableStateFlow<InvoiceUiState>(InvoiceUiState.Idle)
    val invoiceState: StateFlow<InvoiceUiState> = _invoiceState.asStateFlow()

    // پیشرفت پخش (۰ تا ۱)
    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null

    // ============ Init ============
    init {
        load()
    }

    // ============ بارگذاری (اول چک پرو، بعد لیست) ============
    fun load() {
        viewModelScope.launch {
            _uiState.value = PoemsUiState.Loading

            val userId = preferencesManager.user?.id
                    ?: GUEST_USER_ID

            // ۱) وضعیت پرو
            when (val premiumResult = premiumRepository.getPremiumStatus(userId)) {
                is Result.Error   -> {
                    _uiState.value = PoemsUiState.Error(premiumResult.message)
                    return@launch
                }

                is Result.Success -> {
                    if (!premiumResult.data.isPremium) {
                        _uiState.value = PoemsUiState.Guest
                        return@launch
                    }
                }

                is Result.Loading -> { /* ignore */
                }
            }

            // ۲) لیست شعرهای دارای سبک
            when (val result = premiumRepository.getPoemsWithAudio(userId)) {
                is Result.Success -> {
                    val items = result.data.contents?.filterNotNull()
                            ?: emptyList()
                    _poems.value = items
                    _uiState.value = PoemsUiState.Success
                    logD("load: ✅ ${items.size} poems with audio")
                }

                is Result.Error   -> {
                    _uiState.value = PoemsUiState.Error(result.message)
                }

                is Result.Loading -> { /* ignore */
                }
            }
        }
    }

    /**
     * فعال‌سازی نسخه پرو از طریق خرید بازار،
     * و سپس بارگذاری خودکار لیست.
     */
    fun activateFromBazaar(purchaseData: String) {
        viewModelScope.launch {
            val token = try {
                org.json.JSONObject(purchaseData)
                    .optString("purchaseToken")
            } catch (e: Exception) {
                Log.e(
                    "PoemsVM",
                    "parse purchaseData error: $e"
                )
                ""
            }

            if (token.isEmpty()) {
                _uiState.value = PoemsUiState.Error("اطلاعات خرید کامل نیست")
                return@launch
            }

            when (val result = premiumRepository.activateFromBazaar(token)) {
                is Result.Success -> {
                    logD("activateFromBazaar: ✅ activated (already=${result.data.already})")
                    load()
                }

                is Result.Error   -> {
                    _uiState.value = PoemsUiState.Error(result.message)
                }

                is Result.Loading -> { /* ignore */
                }
            }
        }
    }

    /**
     * ساخت فاکتور زرین‌پال (کانال دوم)
     */
    fun startZarinpalPayment(onResult: (payUrl: String?, error: String?) -> Unit) {
        viewModelScope.launch {
            _invoiceState.value = InvoiceUiState.Creating

            val userId = preferencesManager.user?.id
                    ?: GUEST_USER_ID
            when (val result = premiumRepository.createZarinpalInvoice(userId)) {
                is Result.Success -> {
                    _invoiceState.value = InvoiceUiState.Waiting
                    onResult(
                        result.data.payUrl,
                        null
                    )
                }

                is Result.Error   -> {
                    _invoiceState.value = InvoiceUiState.Error(result.message)
                    onResult(
                        null,
                        result.message
                    )
                }

                is Result.Loading -> { /* ignore */
                }
            }
        }
    }

    fun resetInvoiceState() {
        _invoiceState.value = InvoiceUiState.Idle
    }

    // ============ پخش صوت ============
    fun togglePlay(content: Content) {
        val url = content.audioUrl
        if (url.isNullOrBlank()) return

        if (_playingId.value == content.id) {
            stop()
        } else {
            play(
                url,
                content.id
            )
        }
    }

    private fun play(
        url: String,
        id: Int
    ) {
        stop()

        val mp = MediaPlayer()
        try {
            mp.setDataSource(url)
            mp.setOnPreparedListener { it.start() }
            mp.setOnCompletionListener {
                _playingId.value = null
                _progress.value = 0f
                mediaPlayer?.release()
                mediaPlayer = null
            }
            mp.setOnErrorListener { player, _, _ ->
                Log.e(
                    "PoemsVM",
                    "MediaPlayer error"
                )
                _playingId.value = null
                _progress.value = 0f
                player.release()
                if (mediaPlayer === player) mediaPlayer = null
                true
            }
            mp.prepareAsync()
            mediaPlayer = mp
            _playingId.value = id

            // آپدیت نوار پیشرفت هر ثانیه
            viewModelScope.launch {
                while (isActive && _playingId.value == id && mp.isPlaying) {
                    try {
                        val duration = mp.duration
                        if (duration > 0) {
                            _progress.value = mp.currentPosition.toFloat() / duration
                        }
                    } catch (e: Exception) {
                        break
                    }
                    delay(1000)
                }
            }
        } catch (e: Exception) {
            Log.e(
                "PoemsVM",
                "play error: $e"
            )
            mp.release()
        }
    }

    fun stop() {
        mediaPlayer?.let {
            try {
                if (it.isPlaying) it.stop()
                it.release()
            } catch (e: Exception) {
                Log.e(
                    "PoemsVM",
                    "stop error: $e"
                )
            }
        }
        mediaPlayer = null
        _playingId.value = null
        _progress.value = 0f
    }

    override fun onCleared() {
        super.onCleared()
        stop()
    }
}

// ============ UI State ============
sealed class PoemsUiState {
    object Loading : PoemsUiState()
    object Success : PoemsUiState()
    object Guest : PoemsUiState()   // نسخه پرو فعال نیست
    data class Error(val message: String) : PoemsUiState()
}
