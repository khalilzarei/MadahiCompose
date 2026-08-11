package com.khz.madahi.models.response

import com.google.gson.annotations.SerializedName


class ResultResponse {
    @SerializedName("success")
    var success: Boolean = false

    @SerializedName("message")
    var message: String? = null
}

