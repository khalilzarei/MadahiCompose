package com.khz.madahi.models.response

import com.google.gson.annotations.SerializedName
import com.khz.madahi.models.Favorite

data class InsertFavoriteResponse(
    @SerializedName("error") val error: Boolean? = null,
    @SerializedName("error_msg") val errorMsg: String? = null,
    @SerializedName("favorite") val favorite: Favorite? = null,
    @SerializedName("action") val action: String? = null  // ✅ اضافه شد: "added" یا "removed"
)