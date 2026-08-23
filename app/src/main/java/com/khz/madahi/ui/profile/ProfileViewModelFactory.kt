// ui/profile/ProfileViewModelFactory.kt
package com.khz.madahi.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager

class ProfileViewModelFactory(
    private val preferencesManager: PreferencesManager,
    private val appDatabase: AppDatabase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return ProfileViewModel(
                preferencesManager = preferencesManager,
                appDatabase = appDatabase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
