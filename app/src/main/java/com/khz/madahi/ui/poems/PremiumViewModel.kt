// ui/poems/PremiumViewModel.kt
package com.khz.madahi.ui.poems

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.repository.PremiumRepository
import com.khz.madahi.helper.GUEST_USER_ID
import com.khz.madahi.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class PremiumViewModel(
    private val preferencesManager: PreferencesManager,
    private val premiumRepository: PremiumRepository
) : ViewModel() {

    // ============ State ============
    private val _premiumState = MutableStateFlow<PremiumUiState>(PremiumUiState.Checking)
    val premiumState: StateFlow<PremiumUiState> = _premiumState.asStateFlow()

    // وضعیت فاکتور زرین‌پال
    private val _invoiceState = MutableStateFlow<InvoiceUiState>(InvoiceUiState.Idle)
    val invoiceState: StateFlow<InvoiceUiState> = _invoiceState.asStateFlow()

    init {
        checkPremium()
    }

    // ============ بررسی وضعیت پرو ============
    fun checkPremium() {
        viewModelScope.launch {
            _premiumState.value = PremiumUiState.Checking

            val userId = preferencesManager.user?.id
                    ?: GUEST_USER_ID

            when (val result = premiumRepository.getPremiumStatus(userId)) {
                is Result.Success -> {
                    _premiumState.value = if (result.data.isPremium) {
                        PremiumUiState.Pro
                    } else {
                        PremiumUiState.Guest
                    }
                }

                is Result.Error   -> {
                    _premiumState.value = PremiumUiState.Error(result.message)
                }

                is Result.Loading -> { /* ignore */
                }
            }
        }
    }

    /**
     * فعال‌سازی از طریق خرید بازار.
     * [purchaseData] = رشته JSON کلید INAPP_PURCHASE_DATA (حاوی purchaseToken)
     */
    fun activateFromBazaar(purchaseData: String) {
        viewModelScope.launch {
            _premiumState.value = PremiumUiState.Activating

            val token = try {
                JSONObject(purchaseData).optString("purchaseToken")
            } catch (e: Exception) {
                Log.e(
                    "PremiumVM",
                    "parse purchaseData error: $e"
                )
                ""
            }

            if (token.isEmpty()) {
                _premiumState.value = PremiumUiState.Error("اطلاعات خرید کامل نیست")
                return@launch
            }

            when (val result = premiumRepository.activateFromBazaar(token)) {
                is Result.Success -> {
                    _premiumState.value = PremiumUiState.Pro
                }

                is Result.Error   -> {
                    _premiumState.value = PremiumUiState.Error(result.message)
                }

                is Result.Loading -> { /* ignore */
                }
            }
        }
    }

    /**
     * ساخت فاکتور زرین‌پال؛ آدرس پرداخت برای باز شدن برمی‌گردد.
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
}

// ============ UI State ============
sealed class PremiumUiState {
    object Checking : PremiumUiState()
    object Guest : PremiumUiState()
    object Pro : PremiumUiState()
    object Activating : PremiumUiState()
    data class Error(val message: String) : PremiumUiState()
}

// وضعیت فاکتور زرین‌پال
sealed class InvoiceUiState {
    object Idle : InvoiceUiState()
    object Creating : InvoiceUiState()
    object Waiting : InvoiceUiState()
    data class Error(val message: String) : InvoiceUiState()
}
