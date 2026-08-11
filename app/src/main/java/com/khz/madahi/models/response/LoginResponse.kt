package com.khz.madahi.models.response

import com.google.gson.annotations.SerializedName
import com.khz.madahi.models.Category
import com.khz.madahi.models.Content
import com.khz.madahi.models.Favorite
import com.khz.madahi.models.User

// ✅ error دیگر nullable نیست (پیش‌فرض false) - همسان با بقیه Response ها
class LoginResponse {
    @SerializedName("user")
    var user: User? = null

    @SerializedName("categories")
    var categories: MutableList<Category>? = null

    @SerializedName("favorites")
    var favorites: MutableList<Favorite>? = null

    @SerializedName("contents")
    var contents: MutableList<Content>? = null

    @SerializedName("error")
    var error: Boolean = false

    @SerializedName("error_msg")
    var errorMsg: String? = null
}