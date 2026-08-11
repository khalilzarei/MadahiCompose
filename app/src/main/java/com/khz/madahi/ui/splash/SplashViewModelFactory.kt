// ui/splash/SplashViewModelFactory.kt
package com.khz.madahi.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.repository.AppInfoRepository
import com.khz.madahi.utils.NetworkChecker

class SplashViewModelFactory(
    private val preferencesManager: PreferencesManager,
    private val appInfoRepository: AppInfoRepository,
    private val networkChecker: NetworkChecker
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return SplashViewModel(
                preferencesManager,
                appInfoRepository,
                networkChecker
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}