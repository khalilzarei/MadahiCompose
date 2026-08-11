// data/local/database/dao/CategoryDAO.kt
package com.khz.madahi.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.khz.madahi.models.Category

// ✅ همه‌ی متدها suspend شدند تا Room به‌صورت خودکار آن‌ها را
// روی یک IO dispatcher اجرا کند و دیگر نیازی به allowMainThreadQueries نباشد.
@Dao
interface CategoryDAO {

    @Query("SELECT * FROM table_name_categories ORDER BY idCategory DESC")
    suspend fun getAll(): List<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<Category>)

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Query("DELETE FROM table_name_categories WHERE id = :categoryId")
    suspend fun deleteById(categoryId: String)

    @Query("DELETE FROM table_name_categories")
    suspend fun deleteAll()

    @Query("SELECT * FROM table_name_categories WHERE id = :categoryId")
    suspend fun getById(categoryId: String): Category?
}