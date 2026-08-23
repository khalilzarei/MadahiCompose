// ui/message/MessageViewModel.kt
package com.khz.madahi.ui.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.repository.MessageRepository
import com.khz.madahi.helper.GUEST_USER_ID
import com.khz.madahi.helper.extention.logD
import com.khz.madahi.models.MessageItem
import com.khz.madahi.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MessageViewModel(
    private val preferencesManager: PreferencesManager,
    private val messageRepository: MessageRepository
) : ViewModel() {

    // ============ State ============
    private val _uiState = MutableStateFlow<MessageUiState>(MessageUiState.Loading)
    val uiState: StateFlow<MessageUiState> = _uiState.asStateFlow()

    private val _messages = MutableStateFlow<List<MessageItem>>(emptyList())
    val messages: StateFlow<List<MessageItem>> = _messages.asStateFlow()

    // ============ Init ============
    init {
        loadMessages()
    }

    // ============ Load Messages ============
    fun loadMessages() {
        viewModelScope.launch {
            _uiState.value = MessageUiState.Loading

            val userId = preferencesManager.user?.id
                    ?: GUEST_USER_ID

            when (val result = messageRepository.getMessages(userId)) {
                is Result.Success -> {
                    logD("loadMessages: ✅ ${result.data.size} items")
                    _messages.value = result.data
                    _uiState.value = MessageUiState.Success
                }

                is Result.Error   -> {
                    _uiState.value = MessageUiState.Error(result.message)
                }

                is Result.Loading -> { /* ignore */
                }
            }
        }
    }
}

// ============ UI State ============
sealed class MessageUiState {
    object Loading : MessageUiState()
    object Success : MessageUiState()
    data class Error(val message: String) : MessageUiState()
}
