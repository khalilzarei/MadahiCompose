// models/response/LoginResponse.kt
package com.khz.madahi.models.response

import com.google.gson.annotations.SerializedName
import com.khz.madahi.models.Category
import com.khz.madahi.models.Content
import com.khz.madahi.models.Favorite
import com.khz.madahi.models.User

// ✅ هماهنگ با سرور امن — فیلد token اضافه شد
class LoginResponse {
    @SerializedName("token")
    var token: String? = null          // ✅ توکن احراز هویت (Bearer)

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
