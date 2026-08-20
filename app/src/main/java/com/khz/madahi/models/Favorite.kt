package com.khz.madahi.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.khz.madahi.helper.TABLE_NAME_FAVORITE
import java.io.Serializable

@Entity(tableName = TABLE_NAME_FAVORITE)
data class Favorite(
    @PrimaryKey(autoGenerate = true) var idFavorite: Int = 0,
    @SerializedName("content_id") var contentId: Int,
    @SerializedName("id") var id: Int,
    @SerializedName("user_id") var userId: Int,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("update_at") var updateAt: String? = null
) : Serializable
