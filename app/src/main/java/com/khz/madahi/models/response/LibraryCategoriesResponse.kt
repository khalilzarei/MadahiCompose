package com.khz.madahi.models.response

import com.google.gson.annotations.SerializedName
import com.khz.madahi.models.Category

// پاسخ api/getLibraryCategories.php — دسته‌های کتابچه با صفحه‌بندی
class LibraryCategoriesResponse {
    @SerializedName("error")
    var error: Boolean = false

    @SerializedName("error_msg")
    var errorMsg: String? = null

    @SerializedName("data")
    var categories: MutableList<Category?>? = null

    @SerializedName("total")
    var total: Int = 0

    @SerializedName("page")
    var page: Int = 1

    @SerializedName("pages")
    var pages: Int = 1
}
