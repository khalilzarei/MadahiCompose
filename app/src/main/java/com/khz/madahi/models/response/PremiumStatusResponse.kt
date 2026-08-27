package com.khz.madahi.models.response

import com.google.gson.annotations.SerializedName

// پاسخ api/getPremiumStatus.php
class PremiumStatusResponse {
    @SerializedName("error")
    var error: Boolean = false

    @SerializedName("error_msg")
    var errorMsg: String? = null

    @SerializedName("is_premium")
    var isPremium: Boolean = false

    @SerializedName("premium_source")
    var premiumSource: String? = null

    @SerializedName("premium_at")
    var premiumAt: String? = null
}
