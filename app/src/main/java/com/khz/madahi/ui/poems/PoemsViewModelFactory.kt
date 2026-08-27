// ui/poems/PoemsViewModelFactory.kt
package com.khz.madahi.ui.poems

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.api.RetrofitClient
import com.khz.madahi.data.remote.repository.PremiumRepository

class PoemsViewModelFactory(
    private val preferencesManager: PreferencesManager,
    private val appDatabase: AppDatabase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PoemsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return PoemsViewModel(
                preferencesManager = preferencesManager,
                premiumRepository = PremiumRepository(
                    apiService = RetrofitClient.apiService
                ),
                appDatabase = appDatabase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
