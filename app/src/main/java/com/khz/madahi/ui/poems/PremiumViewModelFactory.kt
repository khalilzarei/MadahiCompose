// ui/poems/PremiumViewModelFactory.kt
package com.khz.madahi.ui.poems

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.api.RetrofitClient
import com.khz.madahi.data.remote.repository.PremiumRepository

class PremiumViewModelFactory(
    private val preferencesManager: PreferencesManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PremiumViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return PremiumViewModel(
                preferencesManager = preferencesManager,
                premiumRepository = PremiumRepository(
                    apiService = RetrofitClient.apiService
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
