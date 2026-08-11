package com.khz.madahi.models.response

import com.google.gson.annotations.SerializedName
import com.khz.madahi.models.AppInfo

// ✅ error دیگر nullable نیست (پیش‌فرض false) تا در همه‌ی Response ها
// یک الگوی یکسان رعایت شود و چک‌های `response.error == true` همیشه
// رفتار قابل‌پیش‌بینی داشته باشند، حتی اگر سرور کلید error را نفرستد.
class AppInfoResponse {
    @SerializedName("error")
    var error: Boolean = false

    @SerializedName("error_msg")
    var errorMsg: String? = null

    @SerializedName("info")
    var appInfo: AppInfo? = null
}