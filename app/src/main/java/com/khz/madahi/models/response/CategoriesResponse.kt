package com.khz.madahi.models.response

import com.google.gson.annotations.SerializedName
import com.khz.madahi.models.Category

// ✅ error دیگر nullable نیست (پیش‌فرض false) - همسان با بقیه Response ها
data class CategoriesResponse(
    @SerializedName("error") val error: Boolean = false,
    @SerializedName("error_msg") val errorMsg: String? = null,
    @SerializedName("data") val data: List<Category>? = null  // ✅ داده‌ها در کلید "data" هستند
) {
    // ✅ برای دسترسی راحت‌تر به categories
    val categories: List<Category>
        get() = data
                ?: emptyList()
}