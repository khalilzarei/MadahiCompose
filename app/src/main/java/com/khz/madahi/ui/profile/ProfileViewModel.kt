// ui/profile/ProfileViewModel.kt
package com.khz.madahi.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.helper.GUEST_USER_ID
import com.khz.madahi.helper.extention.logD
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val preferencesManager: PreferencesManager,
    private val appDatabase: AppDatabase
) : ViewModel() {

    // ============ Models ============
    data class ProfileStats(
        val fullName: String,
        val mobile: String,
        val categoryCount: Int,
        val nohehCount: Int,
        val roozehCount: Int
    ) {
        companion object {
            val GUEST = ProfileStats(
                "کاربر مهمان",
                "—",
                0,
                0,
                0
            )
        }
    }

    // ============ State ============
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(preferencesManager.isLoggedIn)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _stats = MutableStateFlow(ProfileStats.GUEST)
    val stats: StateFlow<ProfileStats> = _stats.asStateFlow()

    // ============ Init ============
    init {
        loadProfile()
    }

    // ============ Load Profile (اطلاعات + آمار از دیتابیس محلی) ============
    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            _isLoggedIn.value = preferencesManager.isLoggedIn

            val user = preferencesManager.user

            // ✅ کاربر مهمان — آسازی شخصی نداریم
            if (user == null) {
                _stats.value = ProfileStats.GUEST
                _uiState.value = ProfileUiState.Success
                return@launch
            }

            try {
                val userId = user.id
                        ?: GUEST_USER_ID

                val categoryCount = appDatabase.categoryDAO()
                    .countByUserId(userId)
                val nohehCount = appDatabase.contentDAO()
                    .countByUserIdAndType(
                        userId,
                        "0"
                    )
                val roozehCount = appDatabase.contentDAO()
                    .countByUserIdAndType(
                        userId,
                        "1"
                    )

                _stats.value = ProfileStats(
                    fullName = user.fullName
                            ?: "کاربر",
                    mobile = user.mobile
                            ?: "—",
                    categoryCount = categoryCount,
                    nohehCount = nohehCount,
                    roozehCount = roozehCount
                )
                _uiState.value = ProfileUiState.Success
                logD(
                    "loadProfile: ✅ cat=$categoryCount noheh=$nohehCount roozeh=$roozehCount"
                )
            } catch (e: Exception) {
                logD("loadProfile: error $e")
                _stats.value = ProfileStats(
                    fullName = user.fullName
                            ?: "کاربر",
                    mobile = user.mobile
                            ?: "—",
                    categoryCount = 0,
                    nohehCount = 0,
                    roozehCount = 0
                )
                _uiState.value = ProfileUiState.Success
            }
        }
    }

    // ============ Logout (پاک‌سازی نشست) ============
    fun logout() {
        preferencesManager.clearSession()
        _isLoggedIn.value = false
        _stats.value = ProfileStats.GUEST
        logD("logout: ✅ session cleared")
    }
}

// ============ UI State ============
sealed class ProfileUiState {
    object Loading : ProfileUiState()
    object Success : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}
