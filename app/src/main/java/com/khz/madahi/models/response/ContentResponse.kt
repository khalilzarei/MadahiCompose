package com.khz.madahi.models.response

import com.google.gson.annotations.SerializedName
import com.khz.madahi.models.Content

class ContentResponse {
    @SerializedName("error")
    var error: Boolean = false

    @SerializedName("error_msg")
    var errorMsg: String? = null

    @SerializedName("content")
    var content: Content? = null
}
