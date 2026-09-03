// ui/booklet/BookletModels.kt
package com.khz.madahi.ui.booklet

// ============================================================
// مدل‌های اختصاصی کتابچه — کاملاً مستقل از دفترچه
// ------------------------------------------------------------
// این مدل‌ها برای ساختار کتابچه طراحی شده‌اند
// و هیچ ارتباطی با مدل‌های Category/Content ندارند
// ============================================================

/**
 * بخش اصلی کتابچه (مثل: ادعیه، زیارات، متون محرم و...)
 */
data class BookletSection(
    val id: Int,
    val title: String,
    val description: String,
    val icon: String,          // ایموجی
    val itemCount: Int = 0     // تعداد متون داخل بخش
)

/**
 * متن داخل هر بخش کتابچه
 */
data class BookletItem(
    val id: Int,
    val sectionId: Int,
    val title: String,
    val content: String,
    val source: String = ""    // منبع (مثل: مفاتیح الجنان)
)

/**
 * وضعیت صفحه اصلی کتابچه
 */
sealed class BookletUiState {
    object Loading : BookletUiState()
    data class Success(val sections: List<BookletSection>) : BookletUiState()
    data class Error(val message: String) : BookletUiState()
}

/**
 * وضعیت صفحه جزئیات کتابچه
 */
sealed class BookletDetailUiState {
    object Loading : BookletDetailUiState()
    data class Success(
        val section: BookletSection,
        val items: List<BookletItem>
    ) : BookletDetailUiState()
    data class Error(val message: String) : BookletDetailUiState()
}
