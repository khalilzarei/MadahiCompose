// ui/booklet/BookletViewModel.kt
package com.khz.madahi.ui.booklet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.khz.madahi.data.remote.api.RetrofitClient
import com.khz.madahi.data.remote.repository.BookletRepository
import com.khz.madahi.utils.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// ============================================================
// ViewModel صفحه اصلی کتابچه — بارگذاری صفحه‌به‌صفحه (۳۰ تایی)
// ------------------------------------------------------------
// - صفحه اول ۳۰ دسته برمی‌گردد؛ با رسیدن به انتهای لیست (اسکرول)
//   ۳۰ تای بعدی اضافه می‌شود و همین‌طور تا آخر.
// - جستجو، بارگذاری را از صفحه اول شروع می‌کند.
// ============================================================

class BookletViewModel(
    private val bookletRepository: BookletRepository
) : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 30
    }

    private val _uiState = MutableStateFlow<BookletUiState>(BookletUiState.Loading)
    val uiState: StateFlow<BookletUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // وضعیت صفحه‌بندی
    private var currentPage = 0
    private var totalPages = 1
    private var isLoadingMore = false

    init {
        viewModelScope.launch {
            searchQuery.collectLatest { q ->
                delay(350)
                reload(q)
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    /** تلاش مجدد — بارگذاری از صفحه اول با کوئری فعلی */
    fun loadSections() {
        viewModelScope.launch { reload(_searchQuery.value) }
    }

    /** بارگذاری صفحه بعد — وقتی کاربر به انتهای لیست رسید */
    fun loadMore() {
        val current = _uiState.value as? BookletUiState.Success
                ?: return
        if (isLoadingMore || !current.hasMore) return

        viewModelScope.launch {
            isLoadingMore = true
            _uiState.value = current.copy(isLoadingMore = true)

            val nextPage = currentPage + 1
            when (val result = bookletRepository.getCategories(
                q = _searchQuery.value,
                page = nextPage,
                limit = PAGE_SIZE
            )) {
                is Result.Success -> {
                    currentPage = result.data.page
                    totalPages = result.data.pages

                    val newSections = result.data.items.filter {
                        (it.poemCount
                                ?: 0) > 0
                    }   // حذف دسته‌های بدون شعر
                        .map { category ->
                            BookletSection(
                                id = category.id,
                                title = category.title.ifBlank { "بدون عنوان" },
                                description = category.description,
                                icon = "📄",
                                itemCount = category.poemCount
                                        ?: 0
                            )
                        }
                        .sortedByDescending { it.id }

                    val merged = (current.sections + newSections).distinctBy { it.id }

                    _uiState.value = BookletUiState.Success(
                        sections = merged,
                        isLoadingMore = false,
                        hasMore = currentPage < totalPages
                    )
                }

                is Result.Error   -> {
                    // لیست فعلی را نگه می‌داریم؛ تلاش بعدی با اسکرول دوباره انجام می‌شود
                    _uiState.value = current.copy(isLoadingMore = false)
                }

                is Result.Loading -> { /* ignore */
                }
            }

            isLoadingMore = false
        }
    }

    private suspend fun reload(q: String) {
        _uiState.value = BookletUiState.Loading

        when (val result = bookletRepository.getCategories(
            q = q,
            page = 1,
            limit = PAGE_SIZE
        )) {
            is Result.Success -> {
                currentPage = result.data.page
                totalPages = result.data.pages

                val sections = result.data.items.filter {
                    (it.poemCount
                            ?: 0) > 0
                }   // حذف دسته‌های بدون شعر
                    .map { category ->
                        BookletSection(
                            id = category.id,
                            title = category.title.ifBlank { "بدون عنوان" },
                            description = category.description,
                            icon = "📄",
                            itemCount = category.poemCount
                                    ?: 0
                        )
                    }
                    .sortedByDescending { it.id }

                _uiState.value = BookletUiState.Success(
                    sections = sections,
                    isLoadingMore = false,
                    hasMore = currentPage < totalPages
                )
            }

            is Result.Error   -> {
                _uiState.value = BookletUiState.Error(result.message)
            }

            is Result.Loading -> { /* ignore */
            }
        }
    }
}

// ============================================================
// ViewModel صفحه جزئیات هر بخش کتابچه — بارگذاری صفحه‌به‌صفحه
// ============================================================

class BookletDetailViewModel(
    private val sectionId: Int,
    private val sectionTitle: String,
    private val bookletRepository: BookletRepository
) : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 30
    }

    private val _uiState = MutableStateFlow<BookletDetailUiState>(BookletDetailUiState.Loading)
    val uiState: StateFlow<BookletDetailUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var currentPage = 0
    private var totalPages = 1
    private var isLoadingMore = false

    init {
        viewModelScope.launch {
            searchQuery.collectLatest { q ->
                delay(350)
                reload(q)
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun loadItems() {
        viewModelScope.launch { reload(_searchQuery.value) }
    }

    /** بارگذاری صفحه بعد — وقتی کاربر به انتهای لیست شعرها رسید */
    fun loadMore() {
        val current = _uiState.value as? BookletDetailUiState.Success
                ?: return
        if (isLoadingMore || !current.hasMore) return

        viewModelScope.launch {
            isLoadingMore = true
            _uiState.value = current.copy(isLoadingMore = true)

            val nextPage = currentPage + 1
            when (val result = bookletRepository.getContents(
                categoryId = sectionId,
                q = _searchQuery.value,
                page = nextPage,
                limit = PAGE_SIZE
            )) {
                is Result.Success -> {
                    currentPage = result.data.page
                    totalPages = result.data.pages

                    val newItems = result.data.items.map { poem ->
                        BookletItem(
                            id = poem.id,
                            sectionId = sectionId,
                            title = poem.subject
                                    ?: "",
                            content = poem.content
                                    ?: "",
                            publisherName = poem.publisherName
                                    ?: "",
                            style = poem.style
                                    ?: ""
                        )
                    }

                    val merged = (current.items + newItems).distinctBy { it.id }

                    _uiState.value = BookletDetailUiState.Success(
                        section = current.section,
                        items = merged,
                        isLoadingMore = false,
                        hasMore = currentPage < totalPages
                    )
                }

                is Result.Error   -> {
                    _uiState.value = current.copy(isLoadingMore = false)
                }

                is Result.Loading -> { /* ignore */
                }
            }

            isLoadingMore = false
        }
    }

    private suspend fun reload(q: String) {
        _uiState.value = BookletDetailUiState.Loading

        when (val result = bookletRepository.getContents(
            categoryId = sectionId,
            q = q,
            page = 1,
            limit = PAGE_SIZE
        )) {
            is Result.Success -> {
                currentPage = result.data.page
                totalPages = result.data.pages

                val section = BookletSection(
                    id = sectionId,
                    title = sectionTitle,
                    description = "",
                    icon = ""
                )

                val items = result.data.items.map { poem ->
                    BookletItem(
                        id = poem.id,
                        sectionId = sectionId,
                        title = poem.subject
                                ?: "",
                        content = poem.content
                                ?: "",
                        publisherName = poem.publisherName
                                ?: "",
                        style = poem.style
                                ?: ""
                    )
                }

                _uiState.value = BookletDetailUiState.Success(
                    section = section,
                    items = items,
                    isLoadingMore = false,
                    hasMore = currentPage < totalPages
                )
            }

            is Result.Error   -> {
                _uiState.value = BookletDetailUiState.Error(result.message)
            }

            is Result.Loading -> { /* ignore */
            }
        }
    }
}

// ============================================================
// Factory
// ============================================================

class BookletViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookletViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return BookletViewModel(
                bookletRepository = BookletRepository(RetrofitClient.apiService)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class BookletDetailViewModelFactory(
    private val sectionId: Int,
    private val sectionTitle: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookletDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return BookletDetailViewModel(
                sectionId = sectionId,
                sectionTitle = sectionTitle,
                bookletRepository = BookletRepository(RetrofitClient.apiService)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
