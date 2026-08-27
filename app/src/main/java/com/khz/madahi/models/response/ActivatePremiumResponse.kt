package com.khz.madahi.models.response

import com.google.gson.annotations.SerializedName

// پاسخ api/activatePremiumBazaar.php
class ActivatePremiumResponse {
    @SerializedName("error")
    var error: Boolean = false

    @SerializedName("error_msg")
    var errorMsg: String? = null

    @SerializedName("is_premium")
    var isPremium: Boolean = false

    @SerializedName("already")
    var already: Boolean = false
}
