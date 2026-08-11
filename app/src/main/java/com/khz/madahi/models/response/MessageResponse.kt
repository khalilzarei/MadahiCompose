package com.khz.madahi.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.khz.madahi.models.MessageItem

class MessageResponse {
    @SerializedName("error")
    @Expose
    var error: Boolean = false

    @SerializedName("error_msg")
    @Expose
    var errorMsg: String? = null

    @SerializedName("data")
    var messageItems: MutableList<MessageItem?>? = null
}
