// models/response/ChangePasswordResponse.kt
package com.khz.madahi.models.response

import com.google.gson.annotations.SerializedName

// پاسخ api/changePassword.php
class ChangePasswordResponse {
    @SerializedName("error")
    var error: Boolean = false

    @SerializedName("error_msg")
    var errorMsg: String? = null
}
