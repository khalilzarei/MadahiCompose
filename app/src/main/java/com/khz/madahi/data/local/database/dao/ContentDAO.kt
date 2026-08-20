// data/local/database/dao/ContentDAO.kt
package com.khz.madahi.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.khz.madahi.models.Content

// ✅ همه‌ی متدها suspend شدند
@Dao
interface ContentDAO {

    @Query("SELECT * FROM table_name_content ORDER BY idContent DESC")
    suspend fun getAll(): List<Content>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(content: Content)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contents: List<Content>)

    @Query("DELETE FROM table_name_content WHERE categoryId = :categoryId")
    suspend fun deleteByCategoryId(categoryId: Int)

    @Update
    suspend fun update(content: Content)

    @Delete
    suspend fun delete(content: Content)

    @Query("DELETE FROM table_name_content WHERE id = :contentId")
    suspend fun deleteById(contentId: Int)

    @Query("SELECT * FROM table_name_content WHERE categoryId = :categoryId ORDER BY idContent DESC")
    suspend fun getByCategoryId(categoryId: Int): List<Content>

    @Query("SELECT * FROM table_name_content WHERE id = :contentId")
    suspend fun getById(contentId: Int): Content?

    @Query(
        """
    SELECT DISTINCT * FROM table_name_content 
    WHERE id IN (SELECT DISTINCT contentId FROM table_name_favorite)
    ORDER BY idContent DESC
    """
    )
    suspend fun getFavorites(): List<Content>
}