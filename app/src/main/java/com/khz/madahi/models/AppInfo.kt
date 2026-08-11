package com.khz.madahi.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AppInfo(
    @SerializedName("id") var id: String? = null,
    @SerializedName("version_code") var versionCode: String? = null,
    @SerializedName("version_name") var versionName: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("app_url") var appUrl: String? = null,
    @SerializedName("create_at") var createAt: String? = null
)
