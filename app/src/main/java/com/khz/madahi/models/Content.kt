// models/Content.kt
package com.khz.madahi.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.khz.madahi.helper.TABLE_NAME_CONTENT
import java.io.Serializable

@Entity(
    tableName = TABLE_NAME_CONTENT,
    // ✅ ایندکس یکتا روی شناسه‌ی سرور (همان دلیل Category)
    indices = [Index(
        value = ["id"],
        unique = true
    )]
)
data class Content(
    @PrimaryKey(autoGenerate = true) var idContent: Int = 0,
    @SerializedName("id") var id: Int,
    @SerializedName("category_id") var categoryId: Int,
    @SerializedName("user_id") var userId: Int,
    @SerializedName("answer") var answer: String,
    @SerializedName("content") var content: String,
    @SerializedName("subject") var subject: String,
    @SerializedName("content_type") var contentType: String,
    @SerializedName("audio_url") var audioUrl: String? = null
) : Serializable
