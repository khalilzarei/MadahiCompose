// ui/message/MessageViewModelFactory.kt
package com.khz.madahi.ui.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.api.RetrofitClient
import com.khz.madahi.data.remote.repository.MessageRepository

class MessageViewModelFactory(
    private val preferencesManager: PreferencesManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MessageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return MessageViewModel(
                preferencesManager = preferencesManager,
                messageRepository = MessageRepository(
                    apiService = RetrofitClient.apiService
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
