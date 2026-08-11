package com.khz.madahi.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.khz.madahi.models.Category

class CategoryResponse {
    @SerializedName("error")
    @Expose
    var error: Boolean = false

    @SerializedName("error_msg")
    @Expose
    var errorMsg: String? = null

    @SerializedName("category")
    @Expose
    var category: Category? = null
}
