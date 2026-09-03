// ui/appselection/AppSelectionViewModel.kt
package com.khz.madahi.ui.appselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.api.RetrofitClient
import com.khz.madahi.data.remote.repository.PremiumRepository
import com.khz.madahi.helper.GUEST_USER_ID
import com.khz.madahi.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppSelectionViewModel(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _isPremium = MutableStateFlow(false)
    val isPremium: StateFlow<Boolean> = _isPremium.asStateFlow()

    private val premiumRepository = PremiumRepository(
        apiService = RetrofitClient.apiService
    )

    init {
        checkPremium()
    }

    fun checkPremium() {
        viewModelScope.launch {
            val userId = preferencesManager.user?.id ?: GUEST_USER_ID
            when (val result = premiumRepository.getPremiumStatus(userId)) {
                is Result.Success -> {
                    _isPremium.value = result.data.isPremium
                }
                else -> { /* keep current state */ }
            }
        }
    }

    fun refreshPremium() {
        checkPremium()
    }
}

class AppSelectionViewModelFactory(
    private val preferencesManager: PreferencesManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppSelectionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppSelectionViewModel(preferencesManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
