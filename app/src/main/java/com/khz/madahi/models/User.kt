package com.khz.madahi.models

import com.google.gson.annotations.SerializedName

//@Entity(tableName = Constants.TABLE_USER)
class User(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("full_name") var fullName: String? = null,
    @SerializedName("user_name") var userName: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("mobile") var mobile: String? = null,
    @SerializedName("create_at") var createAt: String? = null,
    @SerializedName("update_at") var updateAt: String? = null
)
