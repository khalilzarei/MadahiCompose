// models/Category.kt
package com.khz.madahi.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.khz.madahi.helper.TABLE_NAME_CATEGORIES
import java.io.Serializable

@Entity(
    tableName = TABLE_NAME_CATEGORIES,
    // ✅ ایندکس یکتا روی شناسه‌ی سرور: با OnConflictStrategy.REPLACE
    // رکوردهای سینک‌شده از سرور به‌جای INSERT مجدد، جایگزین رکورد قبلی
    // می‌شوند و داده‌ها تکراری نمی‌شوند.
    indices = [Index(
        value = ["id"],
        unique = true
    )]
)
data class Category(  // ✅ data class
    @PrimaryKey(autoGenerate = true) var idCategory: Int = 0,
    @SerializedName("user_id") var userId: Int = 0,
    @SerializedName("id") var id: Int = -1,
    @SerializedName("title") var title: String = "",
    @SerializedName("description") var description: String = "",
    @SerializedName("create_at") var createAt: String = "",
    @SerializedName("poem_count") var poemCount: Int? = null
) : Serializable