package com.khz.madahi.models.response

import com.google.gson.annotations.SerializedName
import com.khz.madahi.models.LibraryContent

// پاسخ api/getContentWithId.php — جزئیات یک شعر کتابچه (با publisher_name و style)
class LibraryContentResponse {
    @SerializedName("error")
    var error: Boolean = false

    @SerializedName("error_msg")
    var errorMsg: String? = null

    @SerializedName("content")
    var content: LibraryContent? = null
}
