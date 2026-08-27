// ui/library/LibraryViewModel.kt
package com.khz.madahi.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khz.madahi.data.remote.repository.LibraryRepository
import com.khz.madahi.helper.extention.logD
import com.khz.madahi.models.Category
import com.khz.madahi.utils.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val libraryRepository: LibraryRepository
) : ViewModel() {

    companion object {
        const val PAGE_SIZE = 20
        private const val SEARCH_DEBOUNCE_MS = 400L
    }

    // ============ State ============
    sealed class UiState {
        object Loading : UiState()
        object Success : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _hasMore = MutableStateFlow(false)
    val hasMore: StateFlow<Boolean> = _hasMore.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private var currentPage = 0
    private var searchJob: Job? = null

    init {
        loadPage(1)
    }

    // ============ تغییر جستجو (با debounce) ============
    fun onQueryChange(q: String) {
        _query.value = q
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MS)
            if (_query.value == q) {
                loadPage(1)
            }
        }
    }

    // ============ بارگذاری صفحه ============
    fun loadPage(page: Int) {
        viewModelScope.launch {
            if (page == 1) {
                _uiState.value = UiState.Loading
            } else {
                _isLoadingMore.value = true
            }

            when (val result = libraryRepository.getCategories(
                _query.value,
                page,
                PAGE_SIZE
            )) {
                is Result.Success -> {
                    val items = result.data.categories?.filterNotNull()
                            ?: emptyList()
                    _categories.value = if (page == 1) items else _categories.value + items
                    currentPage = page
                    _hasMore.value = page < result.data.pages
                    _uiState.value = UiState.Success
                    logD("loadPage: page=$page items=${items.size} pages=${result.data.pages}")
                }

                is Result.Error   -> {
                    if (page == 1) {
                        _uiState.value = UiState.Error(result.message)
                    }
                    // خطای صفحه‌های بعدی: لیست قبلی دست‌نخورده می‌ماند
                }

                is Result.Loading -> { /* ignore */
                }
            }

            _isLoadingMore.value = false
        }
    }

    // ============ لود بیشتر (حین اسکرول) ============
    fun loadMore() {
        if (_hasMore.value && !_isLoadingMore.value && _uiState.value is UiState.Success) {
            loadPage(currentPage + 1)
        }
    }
}
