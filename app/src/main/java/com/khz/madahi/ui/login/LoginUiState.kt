package com.khz.madahi.ui.login

// ============ UI State ============
sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()

    // ✅ ورود موفق بوده، ولی رمز هنوز رمز اولیه (= شماره موبایل) است
    // و کاربر باید پیش از ورود به اپ آن را تغییر دهد.
    object NeedsPasswordChange : LoginUiState()

    data class Error(val message: String) : LoginUiState()
}
