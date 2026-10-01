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

    // ✅ true یعنی رمز عبور هنوز رمز اولیه (= شماره موبایل) است و
    // باید پیش از استفاده از اپ تغییر کند.
    @SerializedName("must_change_password")
    var mustChangePassword: Boolean = false

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
