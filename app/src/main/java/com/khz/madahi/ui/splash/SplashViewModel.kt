// ui/splash/SplashViewModel.kt
package com.khz.madahi.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khz.madahi.BuildConfig
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.repository.AppInfoRepository
import com.khz.madahi.utils.NetworkChecker
import com.khz.madahi.utils.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class SplashViewModel(
    private val preferencesManager: PreferencesManager,
    private val appInfoRepository: AppInfoRepository,
    private val networkChecker: NetworkChecker  // ✅ اضافه شد
) : ViewModel() {

    private val _state = MutableStateFlow<SplashState>(SplashState.Loading)
    val state: StateFlow<SplashState> = _state

    init {
        checkAppVersion()
    }

    private fun checkAppVersion() {
        viewModelScope.launch {
            _state.value = SplashState.Loading

            // تاخیر کوتاه برای نمایش Splash
//            delay(300.milliseconds)

            // ✅ بررسی اتصال اینترنت
            if (!networkChecker.isNetworkConnected()) {
                // اگر اینترنت قطع است، بدون بررسی نسخه به صفحه بعد برو
                navigateBasedOnUserState()
                return@launch
            }

            // ✅ دریافت اطلاعات نسخه با timeout 4 ثانیه
            val result = appInfoRepository.getAppInfo()
//            val result = withTimeoutOrNull(3000.milliseconds) {
//                appInfoRepository.getAppInfo()
//            }

            if (result == null) {
                // اگر timeout شد، به صفحه بعد برو
                navigateBasedOnUserState()
                return@launch
            }

            when (result) {
                is Result.Success -> {
                    val appInfo = result.data
                    val currentVersion = BuildConfig.VERSION_CODE
                    val serverVersion = appInfo.versionCode?.toIntOrNull()
                            ?: 0

                    if (serverVersion > currentVersion) {
                        _state.value = SplashState.UpdateRequired(appInfo)
                    } else {
                        navigateBasedOnUserState()
                    }
                }

                is Result.Error   -> {
                    // در صورت خطا، پیغام نمایش داده می‌شود و بعد به صفحه بعد می‌رویم
                    _state.value = SplashState.Error(result.message)
                    delay(1000.milliseconds)
                    navigateBasedOnUserState()
                }

                is Result.Loading -> {
                    // در حال بارگذاری - کاری نمی‌کنیم
                }
            }
        }
    }

    // ui/splash/SplashViewModel.kt
    private fun navigateBasedOnUserState() {
        when {
            preferencesManager.isFirstTimeLaunch -> {
                _state.value = SplashState.NavigateToIntro  // ✅ به Intro برو
            }

            !preferencesManager.isLoggedIn       -> {
                _state.value = SplashState.NavigateToLogin
            }

            else                                 -> {
                _state.value = SplashState.NavigateToCategory
            }
        }
    }
}
