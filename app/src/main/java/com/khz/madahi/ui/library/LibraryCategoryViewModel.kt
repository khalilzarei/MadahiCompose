// ui/library/LibraryCategoryViewModel.kt
package com.khz.madahi.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khz.madahi.data.remote.repository.LibraryRepository
import com.khz.madahi.helper.extention.logD
import com.khz.madahi.models.Content
import com.khz.madahi.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LibraryCategoryViewModel(
    private val libraryRepository: LibraryRepository,
    private val categoryId: Int
) : ViewModel() {

    companion object {
        const val PAGE_SIZE = 20
    }

    // ============ State ============
    sealed class UiState {
        object Loading : UiState()
        object Success : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _poems = MutableStateFlow<List<Content>>(emptyList())
    val poems: StateFlow<List<Content>> = _poems.asStateFlow()

    private val _hasMore = MutableStateFlow(false)
    val hasMore: StateFlow<Boolean> = _hasMore.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private var currentPage = 0

    init {
        loadPage(1)
    }

    fun loadPage(page: Int) {
        viewModelScope.launch {
            if (page == 1) {
                _uiState.value = UiState.Loading
            } else {
                _isLoadingMore.value = true
            }

            when (val result = libraryRepository.getContents(
                categoryId,
                page,
                PAGE_SIZE
            )) {
                is Result.Success -> {
                    val items = result.data.contents?.filterNotNull()
                            ?: emptyList()
                    _poems.value = if (page == 1) items else _poems.value + items
                    currentPage = page
                    _hasMore.value = page < result.data.pages
                    _uiState.value = UiState.Success
                    logD("loadPage: page=$page items=${items.size} pages=${result.data.pages}")
                }

                is Result.Error   -> {
                    if (page == 1) {
                        _uiState.value = UiState.Error(result.message)
                    }
                }

                is Result.Loading -> { /* ignore */
                }
            }

            _isLoadingMore.value = false
        }
    }

    fun loadMore() {
        if (_hasMore.value && !_isLoadingMore.value && _uiState.value is UiState.Success) {
            loadPage(currentPage + 1)
        }
    }
}
