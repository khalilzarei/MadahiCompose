// ui/changepassword/ChangePasswordViewModel.kt
package com.khz.madahi.ui.changepassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.repository.AuthRepository
import com.khz.madahi.helper.PASSWORD_MIN_LENGTH
import com.khz.madahi.utils.NetworkChecker
import com.khz.madahi.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * صفحهٔ اجباری تغییر رمز عبور.
 *
 * کاربرانی که تازه ثبت‌نام کرده‌اند یا از نسخهٔ قبل آمده‌اند، رمز اولیه‌شان
 * همان شمارهٔ موبایل است و سرور تا زمان تغییر رمز، بقیهٔ APIها را می‌بندد.
 */
class ChangePasswordViewModel(
    private val preferencesManager: PreferencesManager,
    private val authRepository: AuthRepository,
    private val networkChecker: NetworkChecker
) : ViewModel() {

    private val _uiState = MutableStateFlow<ChangePasswordUiState>(ChangePasswordUiState.Idle)
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    private val _currentPassword = MutableStateFlow("")
    val currentPassword: StateFlow<String> = _currentPassword.asStateFlow()

    private val _newPassword = MutableStateFlow("")
    val newPassword: StateFlow<String> = _newPassword.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    /** شمارهٔ موبایل کاربر — برای جلوگیری از انتخاب دوبارهٔ آن به‌عنوان رمز */
    val mobileHint: String
        get() = preferencesManager.user?.mobile.orEmpty()

    fun updateCurrentPassword(value: String) {
        _currentPassword.value = value
    }

    fun updateNewPassword(value: String) {
        _newPassword.value = value
    }

    fun updateConfirmPassword(value: String) {
        _confirmPassword.value = value
    }

    fun submit() {
        if (!validateInput()) return

        viewModelScope.launch {
            if (!networkChecker.isNetworkConnected()) {
                _uiState.value = ChangePasswordUiState.Error("لطفاً اتصال اینترنت خود را بررسی کنید")
                return@launch
            }

            _uiState.value = ChangePasswordUiState.Loading

            when (val result = authRepository.changePassword(
                currentPassword = _currentPassword.value,
                newPassword = _newPassword.value
            )) {
                is Result.Success -> {
                    // ✅ از این پس ورود با رمز جدید انجام می‌شود
                    preferencesManager.mustChangePassword = false

                    // رمزها از حافظه پاک شوند
                    _currentPassword.value = ""
                    _newPassword.value = ""
                    _confirmPassword.value = ""

                    _uiState.value = ChangePasswordUiState.Success
                }

                is Result.Error   -> {
                    _uiState.value = ChangePasswordUiState.Error(result.message)
                }

                is Result.Loading -> {
                    // در حال بارگذاری
                }
            }
        }
    }

    // ============ Validation ============
    // اعتبارسنجی اصلی سمت سرور است؛ این‌ها فقط برای پیام‌دهی سریع در اپ.
    private fun validateInput(): Boolean {
        if (_currentPassword.value.isBlank()) {
            _uiState.value = ChangePasswordUiState.Error("لطفاً رمز عبور فعلی را وارد کنید")
            return false
        }

        if (_newPassword.value.isBlank()) {
            _uiState.value = ChangePasswordUiState.Error("لطفاً رمز عبور جدید را وارد کنید")
            return false
        }

        if (_newPassword.value.length < PASSWORD_MIN_LENGTH) {
            _uiState.value = ChangePasswordUiState.Error("رمز عبور جدید باید حداقل $PASSWORD_MIN_LENGTH کاراکتر باشد")
            return false
        }

        if (_newPassword.value != _confirmPassword.value) {
            _uiState.value = ChangePasswordUiState.Error("تکرار رمز عبور جدید مطابقت ندارد")
            return false
        }

        if (_newPassword.value == _currentPassword.value) {
            _uiState.value = ChangePasswordUiState.Error("رمز عبور جدید نباید با رمز فعلی یکسان باشد")
            return false
        }

        if (digitsOnly(_newPassword.value) == digitsOnly(mobileHint)) {
            _uiState.value = ChangePasswordUiState.Error("رمز عبور جدید نباید همان شماره موبایل باشد")
            return false
        }

        return true
    }

    private fun digitsOnly(value: String): String = value.filter { it.isDigit() }
}
