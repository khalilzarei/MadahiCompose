package com.khz.madahi.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.khz.madahi.models.Content

// ✅ error دیگر nullable نیست (پیش‌فرض false) - همسان با بقیه Response ها
class DataResponse {
    @SerializedName("error")
    @Expose
    var error: Boolean = false

    @SerializedName("error_msg")
    @Expose
    var errorMsg: String? = null

    @SerializedName("data")
    @Expose
    var contents: MutableList<Content?>? = null
}