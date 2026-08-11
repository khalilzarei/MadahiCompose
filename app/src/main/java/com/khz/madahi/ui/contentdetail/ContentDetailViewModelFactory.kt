// ui/contentdetail/ContentDetailViewModelFactory.kt
package com.khz.madahi.ui.contentdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.repository.ContentRepository

class ContentDetailViewModelFactory(
    private val preferencesManager: PreferencesManager,
    private val contentRepository: ContentRepository,
    private val appDatabase: AppDatabase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContentDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return ContentDetailViewModel(
                preferencesManager,
                contentRepository,
                appDatabase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}