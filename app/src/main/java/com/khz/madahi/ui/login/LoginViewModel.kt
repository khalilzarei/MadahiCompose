// ui/login/LoginViewModel.kt
package com.khz.madahi.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.repository.AuthRepository
import com.khz.madahi.utils.NetworkChecker
import com.khz.madahi.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class LoginViewModel(
    private val preferencesManager: PreferencesManager,
    private val authRepository: AuthRepository,
    private val networkChecker: NetworkChecker,
    private val appDatabase: AppDatabase
) : ViewModel() {

    // ============ State ============
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _mobile = MutableStateFlow("")
    val mobile: StateFlow<String> = _mobile.asStateFlow()

    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName.asStateFlow()

    private val _isLoginMode = MutableStateFlow(true)
    val isLoginMode: StateFlow<Boolean> = _isLoginMode.asStateFlow()

    // ============ Actions ============
    fun updateMobile(mobile: String) {
        _mobile.value = mobile
    }

    fun updateFullName(name: String) {
        _fullName.value = name
    }

    fun toggleMode() {
        _isLoginMode.value = !_isLoginMode.value
        _uiState.value = LoginUiState.Idle
    }

    fun submit() {
        viewModelScope.launch {
            // اعتبارسنجی
            if (!validateInput()) return@launch

            // بررسی اتصال اینترنت
            if (!networkChecker.isNetworkConnected()) {
                _uiState.value = LoginUiState.Error("لطفاً اتصال اینترنت خود را بررسی کنید")
                return@launch
            }

            _uiState.value = LoginUiState.Loading

            val result = if (_isLoginMode.value) {
                authRepository.login(_mobile.value)
            } else {
                authRepository.register(
                    _mobile.value,
                    _fullName.value
                )
            }

            when (result) {
                is Result.Success -> {
                    val loginResponse = result.data

                    // ✅ ذخیره اطلاعات کاربر
                    preferencesManager.user = loginResponse.user
                    preferencesManager.isLoggedIn = true
                    preferencesManager.isFirstTimeLaunch = false
// ✅ ذخیره توکن برای درخواست‌های بعدی
                    loginResponse.token?.let { preferencesManager.token = it }
                    // ✅ ذخیره دسته‌بندی‌ها در دیتابیس
                    loginResponse.categories?.let { categories ->
                        appDatabase.categoryDAO()
                            .insertAll(categories)
                    }

                    // ✅ ذخیره محتواها در دیتابیس
                    loginResponse.contents?.let { contents ->
                        appDatabase.contentDAO()
                            .insertAll(contents)
                    }

                    // ✅ ذخیره علاقه‌مندی‌ها در دیتابیس
                    loginResponse.favorites?.let { favorites ->
                        appDatabase.favoriteDAO()
                            .insertAll(favorites)
                    }

                    _uiState.value = LoginUiState.Success
                }

                is Result.Error   -> {
                    _uiState.value = LoginUiState.Error(result.message)
                }

                is Result.Loading -> {
                    // در حال بارگذاری
                }
            }
        }
    }

    // ============ Validation ============
    private fun validateInput(): Boolean {
        // بررسی نام (در حالت ثبت نام)
        if (!_isLoginMode.value && _fullName.value.isBlank()) {
            _uiState.value = LoginUiState.Error("لطفاً نام و نام خانوادگی خود را وارد کنید")
            return false
        }

        // بررسی شماره موبایل
        if (_mobile.value.isBlank()) {
            _uiState.value = LoginUiState.Error("لطفاً شماره موبایل خود را وارد کنید")
            return false
        }

        if (!isValidIranianMobile(_mobile.value)) {
            _uiState.value = LoginUiState.Error("لطفاً شماره موبایل معتبر وارد کنید")
            return false
        }

        return true
    }

    private fun isValidIranianMobile(mobile: String): Boolean {
        val pattern = Pattern.compile("(\\+98)?09\\d{9}")
        val matcher = pattern.matcher(mobile.trim())
        return matcher.matches()
    }
}
