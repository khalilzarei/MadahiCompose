// ui/favorite/FavoritesViewModelFactory.kt
package com.khz.madahi.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.repository.ContentRepository

class FavoritesViewModelFactory(
    private val preferencesManager: PreferencesManager,
    private val contentRepository: ContentRepository,
    private val appDatabase: AppDatabase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return FavoritesViewModel(
                preferencesManager,
                contentRepository,
                appDatabase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}