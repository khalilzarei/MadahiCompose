// models/LibraryContent.kt
package com.khz.madahi.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * مدل شعر در «کتابچه» (کتابخانه عمومی) — کاملاً مستقل از Content دفترچه
 *
 * فیلدهای publisher_name و style توسط سرور (getLibraryContents.php) برگردانده می‌شوند.
 */
data class LibraryContent(
    @SerializedName("id") var id: Int = 0,
    @SerializedName("category_id") var categoryId: Int = 0,
    @SerializedName("user_id") var userId: Int = 0,
    @SerializedName("answer") var answer: String? = null,
    @SerializedName("content") var content: String? = null,
    @SerializedName("subject") var subject: String? = null,
    @SerializedName("content_type") var contentType: String? = null,
    @SerializedName("audio_url") var audioUrl: String? = null,
    @SerializedName("publisher_name") var publisherName: String? = null,
    @SerializedName("style") var style: String? = null
) : Serializable {

    /**
     * تبدیل به مدل Content (برای نمایش در ContentDetailScreen)
     * بدون دست‌زدن به مدل Content دفترچه.
     */
    fun toContent(): Content = Content(
        idContent = 0,
        id = id,
        categoryId = categoryId,
        userId = userId,
        answer = answer
                ?: "",
        content = content
                ?: "",
        subject = subject
                ?: "",
        contentType = contentType
                ?: "",
        audioUrl = audioUrl
    )
}
