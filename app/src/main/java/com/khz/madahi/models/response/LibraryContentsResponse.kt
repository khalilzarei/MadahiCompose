package com.khz.madahi.models.response

import com.google.gson.annotations.SerializedName
import com.khz.madahi.models.Content

// پاسخ api/getLibraryContents.php — شعرهای یک دسته با صفحه‌بندی
class LibraryContentsResponse {
    @SerializedName("error")
    var error: Boolean = false

    @SerializedName("error_msg")
    var errorMsg: String? = null

    @SerializedName("data")
    var contents: MutableList<Content?>? = null

    @SerializedName("total")
    var total: Int = 0

    @SerializedName("page")
    var page: Int = 1

    @SerializedName("pages")
    var pages: Int = 1
}
