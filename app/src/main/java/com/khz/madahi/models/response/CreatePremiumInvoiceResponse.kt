package com.khz.madahi.models.response

import com.google.gson.annotations.SerializedName

// پاسخ api/createPremiumInvoice.php (زرین‌پال)
class CreatePremiumInvoiceResponse {
    @SerializedName("error")
    var error: Boolean = false

    @SerializedName("error_msg")
    var errorMsg: String? = null

    @SerializedName("authority")
    var authority: String? = null

    @SerializedName("amount")
    var amount: Int = 0

    @SerializedName("pay_url")
    var payUrl: String? = null

    @SerializedName("referrer")
    var referrer: String? = null
}
