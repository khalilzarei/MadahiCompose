package com.khz.madahi.ui.changepassword

// ============ UI State ============
sealed class ChangePasswordUiState {
    object Idle : ChangePasswordUiState()
    object Loading : ChangePasswordUiState()

    // ✅ رمز با موفقیت تغییر کرد؛ حالا می‌توان وارد اپ شد
    object Success : ChangePasswordUiState()

    data class Error(val message: String) : ChangePasswordUiState()
}
