package com.khz.madahi.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.khz.madahi.helper.TABLE_NAME_CONTENT
import java.io.Serializable


@Entity(tableName = TABLE_NAME_CONTENT)
data class Content(
    @PrimaryKey(autoGenerate = true) var idContent: Int = 0,
    @SerializedName("id") var id: String,
    @SerializedName("category_id") var categoryId: String,
    @SerializedName("user_id") var userId: String,
    @SerializedName("answer") var answer: String,
    @SerializedName("content") var content: String,
    @SerializedName("subject") var subject: String,
    @SerializedName("content_type") var contentType: String
) : Serializable