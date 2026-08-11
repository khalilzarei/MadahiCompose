package com.khz.madahi.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MessageItem(
    @SerializedName("id") var id: String? = null,
    @SerializedName("user_id") var userId: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("created_at") var createdAt: String? = null
) : Serializable
