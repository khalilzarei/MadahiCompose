// models/Category.kt
package com.khz.madahi.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.khz.madahi.helper.TABLE_NAME_CATEGORIES
import java.io.Serializable

@Entity(tableName = TABLE_NAME_CATEGORIES)
data class Category(  // ✅ data class
    @PrimaryKey(autoGenerate = true) var idCategory: Int = 0,
    @SerializedName("user_id") var userId: String? = null,
    @SerializedName("id") var id: String = "",
    @SerializedName("title") var title: String = "",
    @SerializedName("description") var description: String = ""
) : Serializable