// ui/library/LibraryViewModelFactory.kt
package com.khz.madahi.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khz.madahi.data.remote.api.RetrofitClient
import com.khz.madahi.data.remote.repository.LibraryRepository

class LibraryViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LibraryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return LibraryViewModel(
                libraryRepository = LibraryRepository(
                    apiService = RetrofitClient.apiService
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
