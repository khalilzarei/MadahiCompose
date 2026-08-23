// ui/category/CategoryViewModel.kt
package com.khz.madahi.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.repository.CategoryRepository
import com.khz.madahi.helper.GUEST_USER_ID
import com.khz.madahi.helper.extention.logD
import com.khz.madahi.models.Category
import com.khz.madahi.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val preferencesManager: PreferencesManager,
    private val categoryRepository: CategoryRepository,  // ✅ اضافه شد
    private val appDatabase: AppDatabase
) : ViewModel() {

    // ============ State ============
    private val _uiState = MutableStateFlow<CategoryUiState>(CategoryUiState.Loading)
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _isDialogVisible = MutableStateFlow(false)
    val isDialogVisible: StateFlow<Boolean> = _isDialogVisible.asStateFlow()

    private val _editingCategory = MutableStateFlow<Category?>(null)
    val editingCategory: StateFlow<Category?> = _editingCategory.asStateFlow()

    // ✅ خطای فیلد عنوان — زیر همان فیلد نمایش داده می‌شود (مثل setError)
    private val _titleError = MutableStateFlow<String?>(null)
    val titleError: StateFlow<String?> = _titleError.asStateFlow()

    // ✅ خطای فیلد توضیحات — زیر همان فیلد نمایش داده می‌شود (مثل setError)
    private val _descriptionError = MutableStateFlow<String?>(null)
    val descriptionError: StateFlow<String?> = _descriptionError.asStateFlow()

    // ✅ پیام خطای عمومی داخل دیالوگ (مثلاً خطای سرور)
    private val _dialogMessage = MutableStateFlow<String?>(null)
    val dialogMessage: StateFlow<String?> = _dialogMessage.asStateFlow()

    // ============ Dialog Data ============
    private val _dialogTitle = MutableStateFlow("")
    val dialogTitle: StateFlow<String> = _dialogTitle.asStateFlow()

    private val _dialogDescription = MutableStateFlow("")
    val dialogDescription: StateFlow<String> = _dialogDescription.asStateFlow()

    // ============ Init ============
    init {
        loadCategories()
    }

    // ============ Load Categories ============
    fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = CategoryUiState.Loading

            // ✅ ابتدا از دیتابیس محلی بخوان
            val cached = appDatabase.categoryDAO()
                .getAll()
            if (cached.isNotEmpty()) {
                _categories.value = cached
                _uiState.value = CategoryUiState.Success
            }

            // ✅ سپس از سرور همگام‌سازی کن
            val userId = preferencesManager.user?.id
                    ?: GUEST_USER_ID
            when (val result = categoryRepository.getCategories(userId)) {
                is Result.Success -> {
                    _categories.value = result.data
                    _uiState.value = CategoryUiState.Success
                }

                is Result.Error   -> {
                    if (_categories.value.isEmpty()) {
                        _uiState.value = CategoryUiState.Error(result.message)
                    }
                }

                is Result.Loading -> { /* ignore */
                }
            }
        }
    }

    // ============ Dialog Actions ============
    fun showAddCategoryDialog() {
        _dialogTitle.value = ""
        _dialogDescription.value = ""
        _editingCategory.value = null
        _titleError.value = null
        _descriptionError.value = null
        _dialogMessage.value = null
        _isDialogVisible.value = true
    }

    fun showEditCategoryDialog(category: Category) {
        _dialogTitle.value = category.title
        _dialogDescription.value = category.description
        _editingCategory.value = category
        _titleError.value = null
        _descriptionError.value = null
        _dialogMessage.value = null
        _isDialogVisible.value = true
        logD("showEditCategoryDialog ${category.title}")
    }

    fun hideDialog() {
        _isDialogVisible.value = false
        _editingCategory.value = null
        _dialogTitle.value = ""
        _dialogDescription.value = ""
        _titleError.value = null
        _descriptionError.value = null
        _dialogMessage.value = null
    }

    fun updateDialogTitle(title: String) {
        _dialogTitle.value = title
        // ✅ هنگام تایپ، خطای فیلد پاک می‌شود (همان رفتار setError)
        _titleError.value = null
    }

    fun updateDialogDescription(description: String) {
        _dialogDescription.value = description
        // ✅ هنگام تایپ، خطای فیلد پاک می‌شود (همان رفتار setError)
        _descriptionError.value = null
    }

    // ============ Add Category ============
    fun addCategory() {
        viewModelScope.launch {
            val title = _dialogTitle.value.trim()
            val description = _dialogDescription.value.trim()

            // ✅ اعتبارسنجی هر دو فیلد هم‌زمان (هر خطا زیر همان فیلد)
            val titleErr = if (title.isEmpty()) "لطفاً عنوان را وارد کنید" else null
            val descriptionErr = if (description.isEmpty()) "لطفاً توضیحات را وارد کنید" else null
            if (titleErr != null || descriptionErr != null) {
                _titleError.value = titleErr
                _descriptionError.value = descriptionErr
                return@launch
            }

            _uiState.value = CategoryUiState.Loading

            val userId = preferencesManager.user?.id
                    ?: GUEST_USER_ID

            logD("UserID = $userId token= ${preferencesManager.token} ")
            // ✅ به سرور ارسال کن
            when (val result = categoryRepository.addCategory(
                userId,
                title,
                description
            )) {
                is Result.Success -> {
                    val category = result.data

                    // ✅ فقط یک بار در دیتابیس محلی ذخیره کن
                    // ابتدا چک کن که وجود ندارد
                    val existing = appDatabase.categoryDAO()
                        .getById(category.id)
                    if (existing == null) {
                        appDatabase.categoryDAO()
                            .insert(category)
                    }

                    hideDialog()
                    loadCategories()  // ✅ بارگذاری مجدد از دیتابیس (دوبار نمی‌شود)
                }

                is Result.Error   -> {
                    _dialogMessage.value = result.message
                }

                is Result.Loading -> { /* ignore */
                }
            }
        }
    }

    // ============ Edit Category ============
    fun editCategory() {
        viewModelScope.launch {
            val title = _dialogTitle.value.trim()
            val description = _dialogDescription.value.trim()
            val category = _editingCategory.value
                    ?: return@launch

            // ✅ اعتبارسنجی هر دو فیلد هم‌زمان (هر خطا زیر همان فیلد)
            val titleErr = if (title.isEmpty()) "لطفاً عنوان را وارد کنید" else null
            val descriptionErr = if (description.isEmpty()) "لطفاً توضیحات را وارد کنید" else null
            if (titleErr != null || descriptionErr != null) {
                _titleError.value = titleErr
                _descriptionError.value = descriptionErr
                return@launch
            }

            _uiState.value = CategoryUiState.Loading

            val updatedCategory = category.copy(
                title = title,
                description = description
            )

            // ✅ به سرور ارسال کن
            when (val result = categoryRepository.updateCategory(updatedCategory)) {
                is Result.Success -> {
                    // ✅ بروزرسانی در دیتابیس محلی
                    appDatabase.categoryDAO()
                        .update(updatedCategory)
                    hideDialog()
                    loadCategories()
                }

                is Result.Error   -> {
                    _dialogMessage.value = result.message
                }

                is Result.Loading -> { /* ignore */
                }
            }
        }
    }

    // ============ Delete Category ============
    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            _uiState.value = CategoryUiState.Loading

            val userId = preferencesManager.user?.id
                    ?: GUEST_USER_ID

            // ✅ ارسال userId به repository
            when (val result = categoryRepository.deleteCategory(
                userId,
                category.id
            )) {
                is Result.Success -> {
                    loadCategories()
                }

                is Result.Error   -> {
                    _uiState.value = CategoryUiState.Error(result.message)
                }

                is Result.Loading -> { /* ignore */
                }
            }
        }
    }

    fun getCategoryTitle(): String {
        return "دسته‌بندی‌ها"
    }
}

// ============ UI State ============
sealed class CategoryUiState {
    object Loading : CategoryUiState()
    object Success : CategoryUiState()
    data class Error(val message: String) : CategoryUiState()
}