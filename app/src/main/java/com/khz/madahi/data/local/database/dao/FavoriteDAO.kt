// data/local/database/dao/FavoriteDAO.kt
package com.khz.madahi.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.khz.madahi.models.Favorite

// ✅ همه‌ی متدها suspend شدند
@Dao
interface FavoriteDAO {

    @Query("SELECT * FROM table_name_favorite")
    suspend fun getAll(): List<Favorite>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: Favorite)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(favorites: List<Favorite>)

    @Delete
    suspend fun delete(favorite: Favorite)

    @Query("DELETE FROM table_name_favorite WHERE contentId = :contentId")
    suspend fun deleteByContentId(contentId: Int)

    @Query("SELECT * FROM table_name_favorite WHERE contentId = :contentId AND userId = :userId")
    suspend fun getFavorite(
        contentId: Int,
        userId: Int
    ): Favorite?
}