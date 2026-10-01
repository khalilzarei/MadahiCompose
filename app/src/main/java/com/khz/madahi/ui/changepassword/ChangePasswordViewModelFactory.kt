// ui/changepassword/ChangePasswordViewModelFactory.kt
package com.khz.madahi.ui.changepassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.repository.AuthRepository
import com.khz.madahi.utils.NetworkChecker

class ChangePasswordViewModelFactory(
    private val preferencesManager: PreferencesManager,
    private val authRepository: AuthRepository,
    private val networkChecker: NetworkChecker
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChangePasswordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return ChangePasswordViewModel(
                preferencesManager,
                authRepository,
                networkChecker
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
