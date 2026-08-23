// ui/content/ContentViewModel.kt
package com.khz.madahi.ui.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.repository.ContentRepository
import com.khz.madahi.helper.GUEST_USER_ID
import com.khz.madahi.helper.extention.cleanForServer
import com.khz.madahi.models.Category
import com.khz.madahi.models.Content
import com.khz.madahi.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContentViewModel(
    private val preferencesManager: PreferencesManager,
    private val contentRepository: ContentRepository,
    private val appDatabase: AppDatabase,
    private val category: Category?
) : ViewModel() {

    // ============ State ============
    private val _uiState = MutableStateFlow<ContentUiState>(ContentUiState.Loading)
    val uiState: StateFlow<ContentUiState> = _uiState.asStateFlow()

    private val _contents = MutableStateFlow<List<Content>>(emptyList())
    val contents: StateFlow<List<Content>> = _contents.asStateFlow()

    private val _isDialogVisible = MutableStateFlow(false)
    val isDialogVisible: StateFlow<Boolean> = _isDialogVisible.asStateFlow()

    private val _editingContent = MutableStateFlow<Content?>(null)
    val editingContent: StateFlow<Content?> = _editingContent.asStateFlow()

    // ============ Dialog Data ============
    private val _dialogSubject = MutableStateFlow("")
    val dialogSubject: StateFlow<String> = _dialogSubject.asStateFlow()

    private val _dialogAnswer = MutableStateFlow("")
    val dialogAnswer: StateFlow<String> = _dialogAnswer.asStateFlow()

    private val _dialogContent = MutableStateFlow("")
    val dialogContent: StateFlow<String> = _dialogContent.asStateFlow()

    private val _isNoheh = MutableStateFlow(true)
    val isNoheh: StateFlow<Boolean> = _isNoheh.asStateFlow()

    // ✅ خطای فیلد عنوان — زیر همان فیلد نمایش داده می‌شود (مثل setError)
    private val _subjectError = MutableStateFlow<String?>(null)
    val subjectError: StateFlow<String?> = _subjectError.asStateFlow()

    // ✅ خطای فیلد متن — زیر همان فیلد نمایش داده می‌شود (مثل setError)
    private val _contentError = MutableStateFlow<String?>(null)
    val contentError: StateFlow<String?> = _contentError.asStateFlow()

    // ✅ پیام خطای عمومی داخل دیالوگ (مثلاً خطای سرور)
    private val _dialogMessage = MutableStateFlow<String?>(null)
    val dialogMessage: StateFlow<String?> = _dialogMessage.asStateFlow()

    // ============ Init ============
    init {
        loadContentsFromDatabase()  // ✅ اول از دیتابیس
        syncWithServer()            // ✅ سپس همگام‌سازی با سرور
    }

    // ============ 1. بارگذاری از دیتابیس محلی (سریع) ============
    private fun loadContentsFromDatabase() {
        viewModelScope.launch {
            val categoryId = category?.id
                    ?: GUEST_USER_ID

            // ✅ خواندن از دیتابیس محلی
            val cached = appDatabase.contentDAO()
                .getByCategoryId(categoryId)

            if (cached.isNotEmpty()) {
                _contents.value = cached
                _uiState.value = ContentUiState.Success
            } else {
                // اگر دیتابیس خالی بود، منتظر سرور بمان
                _uiState.value = ContentUiState.Loading
            }
        }
    }

    // ============ 2. همگام‌سازی با سرور (در پس‌زمینه) ============
    private fun syncWithServer() {
        viewModelScope.launch {
            val categoryId = category?.id
                    ?: GUEST_USER_ID
            val userId = preferencesManager.user?.id
                    ?: GUEST_USER_ID

            when (val result = contentRepository.getContents(
                categoryId,
                userId
            )) {
                is Result.Success -> {
                    val serverData = result.data
                    if (serverData.isNotEmpty()) {
                        // ✅ بروزرسانی دیتابیس با اطلاعات جدید
                        appDatabase.contentDAO()
                            .insertAll(serverData)
                        // ✅ بروزرسانی UI
                        _contents.value = serverData
                        _uiState.value = ContentUiState.Success
                    }
                }

                is Result.Error   -> {
                    // اگر دیتابیس خالی بود و سرور خطا داد
                    if (_contents.value.isEmpty()) {
                        _uiState.value = ContentUiState.Error(result.message)
                    }
                }

                is Result.Loading -> { /* ignore */
                }
            }
        }
    }

    // ============ بارگذاری مجدد (برای Refresh) ============
    fun refresh() {
        viewModelScope.launch {
            // ✅ اول از دیتابیس
            loadContentsFromDatabase()
            // ✅ سپس همگام‌سازی با سرور
            syncWithServer()
        }
    }

    // ============ بارگذاری کامل (برای بعد از افزودن/ویرایش/حذف) ============
    fun loadContents() {
        refresh()
    }

    // ============ Dialog Actions ============
    fun showAddDialog() {
        _dialogSubject.value = ""
        _dialogAnswer.value = ""
        _dialogContent.value = ""
        _isNoheh.value = true
        _editingContent.value = null
        _subjectError.value = null
        _contentError.value = null
        _dialogMessage.value = null
        _isDialogVisible.value = true
    }

    fun showEditDialog(content: Content) {
        _dialogSubject.value = content.subject
        _dialogAnswer.value = content.answer
        _dialogContent.value = content.content
        _isNoheh.value = content.contentType == "0"
        _editingContent.value = content
        _subjectError.value = null
        _contentError.value = null
        _dialogMessage.value = null
        _isDialogVisible.value = true
    }

    fun hideDialog() {
        _isDialogVisible.value = false
        _editingContent.value = null
        _subjectError.value = null
        _contentError.value = null
        _dialogMessage.value = null
    }

    fun updateSubject(subject: String) {
        _dialogSubject.value = subject
        // ✅ هنگام تایپ، خطای فیلد پاک می‌شود (همان رفتار setError)
        _subjectError.value = null
    }

    fun updateAnswer(answer: String) {
        _dialogAnswer.value = answer
    }

    fun updateContent(content: String) {
        _dialogContent.value = content
        // ✅ هنگام تایپ، خطای فیلد پاک می‌شود (همان رفتار setError)
        _contentError.value = null
    }

    fun updateContentType(isNoheh: Boolean) {
        _isNoheh.value = isNoheh
    }

    // ============ Add/Edit/Delete Content ============
    fun addContent() {
        viewModelScope.launch {
            // ✅ حذف کاراکترهای ۴ بایتی (ایموجی و...) — سرور utf8mb3 است
            val subject = _dialogSubject.value.cleanForServer()
            val answer = _dialogAnswer.value.cleanForServer()
            val contentText = _dialogContent.value.cleanForServer()
            val userId = preferencesManager.user?.id
                    ?: GUEST_USER_ID
            val categoryId = category?.id
                    ?: GUEST_USER_ID

            // ✅ اعتبارسنجی هر دو فیلد هم‌زمان (هر خطا زیر همان فیلد نمایش داده می‌شود)
            val subjectErr = if (subject.isEmpty()) "لطفاً عنوان را وارد کنید" else null
            val contentErr = if (contentText.isEmpty()) "لطفاً متن را وارد کنید" else null
            if (subjectErr != null || contentErr != null) {
                _subjectError.value = subjectErr
                _contentError.value = contentErr
                return@launch
            }

            _uiState.value = ContentUiState.Loading

            val contentType = if (_isNoheh.value) "0" else "1"

            val tempContent = Content(
                idContent = 0,
                id = System.currentTimeMillis()
                    .toInt(),
                categoryId = categoryId,
                userId = userId,
                answer = answer,
                content = "<p>$contentText</p>",
                subject = subject,
                contentType = contentType
            )

            // ✅ ذخیره در دیتابیس محلی
            appDatabase.contentDAO()
                .insert(tempContent)

            when (val result = contentRepository.addContent(tempContent)) {
                is Result.Success -> {
                    hideDialog()
                    refresh()
                }

                is Result.Error   -> {
                    appDatabase.contentDAO()
                        .delete(tempContent)
                    _dialogMessage.value = result.message
                }

                is Result.Loading -> { /* ignore */
                }
            }
        }
    }

    fun editContent() {
        viewModelScope.launch {
            // ✅ حذف کاراکترهای ۴ بایتی (ایموجی و...) — سرور utf8mb3 است
            val subject = _dialogSubject.value.cleanForServer()
            val answer = _dialogAnswer.value.cleanForServer()
            val contentText = _dialogContent.value.cleanForServer()
            val content = _editingContent.value
                    ?: return@launch

            // ✅ اعتبارسنجی هر دو فیلد هم‌زمان (هر خطا زیر همان فیلد نمایش داده می‌شود)
            val subjectErr = if (subject.isEmpty()) "لطفاً عنوان را وارد کنید" else null
            val contentErr = if (contentText.isEmpty()) "لطفاً متن را وارد کنید" else null
            if (subjectErr != null || contentErr != null) {
                _subjectError.value = subjectErr
                _contentError.value = contentErr
                return@launch
            }

            _uiState.value = ContentUiState.Loading

            val updatedContent = content.copy(
                subject = subject,
                answer = answer,
                content = "<p>$contentText</p>",
                contentType = if (_isNoheh.value) "0" else "1"
            )

            // ✅ بروزرسانی در دیتابیس محلی
            appDatabase.contentDAO()
                .update(updatedContent)

            when (val result = contentRepository.updateContent(updatedContent)) {
                is Result.Success -> {
                    hideDialog()
                    refresh()
                }

                is Result.Error   -> {
                    appDatabase.contentDAO()
                        .update(content)
                    _dialogMessage.value = result.message
                }

                is Result.Loading -> { /* ignore */
                }
            }
        }
    }

    fun deleteContent(content: Content) {
        viewModelScope.launch {
            _uiState.value = ContentUiState.Loading

            // ✅ حذف از دیتابیس محلی
            appDatabase.contentDAO()
                .delete(content)
            val userId = preferencesManager.user?.id
                    ?: GUEST_USER_ID
            when (val result = contentRepository.deleteContent(
                userId,
                content.id
            )) {
                is Result.Success -> {
                    refresh()
                }

                is Result.Error   -> {
                    appDatabase.contentDAO()
                        .insert(content)
                    _uiState.value = ContentUiState.Error(result.message)
                }

                is Result.Loading -> { /* ignore */
                }
            }
        }
    }

    fun getCategoryTitle(): String {
        return category?.title
                ?: "همه محتواها"
    }
}

// ============ UI State ============
sealed class ContentUiState {
    object Loading : ContentUiState()
    object Success : ContentUiState()
    data class Error(val message: String) : ContentUiState()
}