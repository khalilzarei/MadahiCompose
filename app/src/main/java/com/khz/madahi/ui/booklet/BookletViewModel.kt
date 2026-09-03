// ui/booklet/BookletViewModel.kt
package com.khz.madahi.ui.booklet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.khz.madahi.data.local.preferences.PreferencesManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ============================================================
// ViewModel صفحه اصلی کتابچه
// ------------------------------------------------------------
// در حال حاضر با داده‌های placeholder کار می‌کند
// بعداً به API واقعی وصل خواهد شد
// ============================================================

class BookletViewModel(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<BookletUiState>(BookletUiState.Loading)
    val uiState: StateFlow<BookletUiState> = _uiState.asStateFlow()

    init {
        loadSections()
    }

    fun loadSections() {
        viewModelScope.launch {
            _uiState.value = BookletUiState.Loading
            delay(500) // شبیه‌سازی لود

            // ✅ داده‌های placeholder — بعداً از API خوانده می‌شود
            val sections = listOf(
                BookletSection(
                    id = 1,
                    title = "دعاهای روزانه",
                    description = "دعاهای صبح، شب و روزهای هفته",
                    icon = "🤲",
                    itemCount = 12
                ),
                BookletSection(
                    id = 2,
                    title = "زیارات",
                    description = "زیارت عاشورا، امام حسین و ائمه",
                    icon = "🕌",
                    itemCount = 8
                ),
                BookletSection(
                    id = 3,
                    title = "متون محرم",
                    description = "مرثیه‌ها و سینه‌زنی‌های محرم",
                    icon = "🌙",
                    itemCount = 15
                ),
                BookletSection(
                    id = 4,
                    title = "متون فاطمیه",
                    description = "مرثیه‌های حضرت زهرا (س)",
                    icon = "🌹",
                    itemCount = 10
                ),
                BookletSection(
                    id = 5,
                    title = "اربعین",
                    description = "متون و ادعیه اربعین حسینی",
                    icon = "🏴",
                    itemCount = 6
                ),
                BookletSection(
                    id = 6,
                    title = "شهادت امام رضا (ع)",
                    description = "متون شهادت امام رضا (ع)",
                    icon = "💚",
                    itemCount = 5
                )
            )

            _uiState.value = BookletUiState.Success(sections)
        }
    }
}

// ============================================================
// ViewModel صفحه جزئیات هر بخش کتابچه
// ============================================================

class BookletDetailViewModel(
    private val sectionId: Int,
    private val sectionTitle: String
) : ViewModel() {

    private val _uiState = MutableStateFlow<BookletDetailUiState>(BookletDetailUiState.Loading)
    val uiState: StateFlow<BookletDetailUiState> = _uiState.asStateFlow()

    init {
        loadItems()
    }

    private fun loadItems() {
        viewModelScope.launch {
            _uiState.value = BookletDetailUiState.Loading
            delay(400)

            val section = BookletSection(
                id = sectionId,
                title = sectionTitle,
                description = "",
                icon = ""
            )

            // ✅ داده‌های placeholder — بعداً از API خوانده می‌شود
            val items = listOf(
                BookletItem(
                    id = 1,
                    sectionId = sectionId,
                    title = "دعای صبحگاهی",
                    content = """
                        اَللّهُمَّ بِکَ صَلَّیْتُ عَلَیْکَ وَ بِکَ نَوَیْتُ
                        وَ بِکَ آمَنْتُ وَ عَلَیْکَ تَوَکَّلْتُ
                        وَ إِلَیْکَ أَنَبْتُ
                    """.trimIndent(),
                    source = "مفاتیح الجنان"
                ),
                BookletItem(
                    id = 2,
                    sectionId = sectionId,
                    title = "دعای کمیل",
                    content = "بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِیمِ\n\nاللَّهُمَّ کَمَا تَجِبُ الرَّحْمَةُ لِمَنْ رَحِمْتَ...",
                    source = "مفاتیح الجنان"
                ),
                BookletItem(
                    id = 3,
                    sectionId = sectionId,
                    title = "دعای فرج",
                    content = "اللَّهُمَّ کُنْ لِوَلِیِّکَ الحُجَّةِ بْنِ الحَسَنِ...",
                    source = "دعای ندبه"
                )
            )

            _uiState.value = BookletDetailUiState.Success(section, items)
        }
    }
}

// ============================================================
// Factory
// ============================================================

class BookletViewModelFactory(
    private val preferencesManager: PreferencesManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookletViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookletViewModel(preferencesManager) as T
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
            @Suppress("UNCHECKED_CAST")
            return BookletDetailViewModel(sectionId, sectionTitle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
