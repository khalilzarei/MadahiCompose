// ui/content/ContentViewModelFactory.kt
package com.khz.madahi.ui.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.repository.ContentRepository
import com.khz.madahi.models.Category

class ContentViewModelFactory(
    private val preferencesManager: PreferencesManager,
    private val contentRepository: ContentRepository,
    private val appDatabase: AppDatabase,
    private val category: Category?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return ContentViewModel(
                preferencesManager,
                contentRepository,
                appDatabase,
                category
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}