// ui/category/CategoryViewModelFactory.kt
package com.khz.madahi.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.api.RetrofitClient
import com.khz.madahi.data.remote.repository.CategoryRepository

class CategoryViewModelFactory(
    private val preferencesManager: PreferencesManager,
    private val appDatabase: AppDatabase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return CategoryViewModel(
                preferencesManager = preferencesManager,
                categoryRepository = CategoryRepository(
                    apiService = RetrofitClient.apiService,
                    categoryDao = appDatabase.categoryDAO()
                ),
                appDatabase = appDatabase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}