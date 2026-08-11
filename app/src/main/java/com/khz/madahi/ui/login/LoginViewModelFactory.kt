// ui/login/LoginViewModelFactory.kt
package com.khz.madahi.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.repository.AuthRepository
import com.khz.madahi.utils.NetworkChecker

class LoginViewModelFactory(
    private val preferencesManager: PreferencesManager,
    private val authRepository: AuthRepository,
    private val networkChecker: NetworkChecker,
    private val appDatabase: AppDatabase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return LoginViewModel(
                preferencesManager,
                authRepository,
                networkChecker,
                appDatabase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}