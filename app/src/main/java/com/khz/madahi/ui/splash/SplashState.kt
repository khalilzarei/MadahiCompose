package com.khz.madahi.ui.splash

import com.khz.madahi.models.AppInfo


// ui/splash/SplashState.kt
sealed class SplashState {
    object Loading : SplashState()
    object NavigateToIntro : SplashState()   // ✅ اضافه شد
    object NavigateToLogin : SplashState()
    object NavigateToCategory : SplashState()
    data class UpdateRequired(val appInfo: AppInfo) : SplashState()
    data class Error(val message: String) : SplashState()
}