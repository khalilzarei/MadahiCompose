// data/remote/repository/CategoryRepository.kt
package com.khz.madahi.data.remote.repository

import android.util.Log
import com.khz.madahi.data.local.database.dao.CategoryDAO
import com.khz.madahi.data.remote.api.APIService
import com.khz.madahi.models.Category
import com.khz.madahi.utils.Result

class CategoryRepository(
    private val apiService: APIService,
    private val categoryDao: CategoryDAO
) {

    companion object {
        private const val TAG = "CategoryRepository"
    }

    // ============ Get Categories (suspend) ============
    suspend fun getCategories(userId: String): Result<List<Category>> {
        return try {
            Log.d(
                TAG,
                "getCategories: userId=$userId"
            )

            // ✅ دریافت از سرور (GET با @Query)
            val response = apiService.getCategories(userId)

            Log.d(
                TAG,
                "getCategories: response=$response"
            )

            // ✅ بررسی خطا از سرور
            if (response.error == true) {
                val errorMsg = response.errorMsg
                        ?: "خطا در دریافت دسته‌بندی‌ها"
                Log.e(
                    TAG,
                    "getCategories: server error=$errorMsg"
                )
                return Result.Error(errorMsg)
            }

            // ✅ دریافت داده‌ها از کلید "data"
            val categories = response.categories
            Log.d(
                TAG,
                "getCategories: categories size=${categories.size}"
            )

            // ✅ ذخیره در دیتابیس
            if (categories.isNotEmpty()) {
                categoryDao.deleteAll()
                categoryDao.insertAll(categories)
            }

            Result.Success(categories)

        } catch (e: Exception) {
            Log.e(
                TAG,
                "getCategories error",
                e
            )

            val cached = categoryDao.getAll()
            if (cached.isNotEmpty()) {
                Log.d(
                    TAG,
                    "getCategories: using cached data, size=${cached.size}"
                )
                return Result.Success(cached)
            }

            Result.Error("خطا در ارتباط با سرور: ${e.message}")
        }
    }

    // ============ Add Category ============
    suspend fun addCategory(
        userId: String,
        title: String,
        description: String
    ): Result<Category> {
        return try {
            Log.d(
                TAG,
                "addCategory: userId=$userId, title=$title"
            )

            val response = apiService.insertCategory(
                userId,
                title,
                description
            )

            Log.d(
                TAG,
                "addCategory: response=$response"
            )

            if (response.error) {
                val errorMsg = response.errorMsg
                        ?: "خطا در افزودن دسته‌بندی"
                Log.e(
                    TAG,
                    "addCategory: server error=$errorMsg"
                )
                return Result.Error(errorMsg)
            }

            val category = response.category
            if (category == null) {
                Log.e(
                    TAG,
                    "addCategory: category is null"
                )
                return Result.Error("دسته‌بندی ایجاد نشد")
            }

            // ✅ قبل از insert چک کن که وجود ندارد
            val existing = categoryDao.getById(category.id)
            if (existing == null) {
                categoryDao.insert(category)
                Log.d(
                    TAG,
                    "addCategory: inserted into DB ✅"
                )
            } else {
                Log.d(
                    TAG,
                    "addCategory: already exists in DB, skipping insert"
                )
            }

            Result.Success(category)

        } catch (e: Exception) {
            Log.e(
                TAG,
                "addCategory error",
                e
            )
            Result.Error("خطا در ارتباط با سرور: ${e.message}")
        }
    }

    // ============ Update Category ============
    suspend fun updateCategory(category: Category): Result<Category> {
        return try {
            Log.d(
                TAG,
                "updateCategory: id=${category.id}"
            )

            val response = apiService.updateCategory(
                category.userId
                        ?: "0",
                category.id,
                category.title,
                category.description
            )

            Log.d(
                TAG,
                "updateCategory: response=$response"
            )

            if (response.error) {
                val errorMsg = response.errorMsg
                        ?: "خطا در ویرایش دسته‌بندی"
                Log.e(
                    TAG,
                    "updateCategory: server error=$errorMsg"
                )
                return Result.Error(errorMsg)
            }

            // ✅ بروزرسانی در دیتابیس محلی
            categoryDao.update(category)
            Log.d(
                TAG,
                "updateCategory: updated in DB ✅"
            )

            Result.Success(category)

        } catch (e: Exception) {
            Log.e(
                TAG,
                "updateCategory error",
                e
            )
            Result.Error("خطا در ارتباط با سرور: ${e.message}")
        }
    }

    // ============ Delete Category ============
    suspend fun deleteCategory(
        userId: String,
        categoryId: String
    ): Result<Boolean> {
        return try {
            Log.d(
                TAG,
                "deleteCategory: userId=$userId, categoryId=$categoryId"
            )

            // ✅ ارسال userId و categoryId به سرور
            val response = apiService.deleteCategory(
                userId,
                categoryId
            )

            Log.d(
                TAG,
                "deleteCategory: response=$response"
            )

            if (!response.success) {
                val errorMsg = response.message
                        ?: "خطا در حذف دسته‌بندی"
                Log.e(
                    TAG,
                    "deleteCategory: server error=$errorMsg"
                )
                return Result.Error(errorMsg)
            }

            // ✅ حذف از دیتابیس محلی
            categoryDao.deleteById(categoryId)
            Log.d(
                TAG,
                "deleteCategory: success ✅"
            )

            Result.Success(true)

        } catch (e: Exception) {
            Log.e(
                TAG,
                "deleteCategory error",
                e
            )
            Result.Error("خطا در ارتباط با سرور: ${e.message}")
        }
    }
}