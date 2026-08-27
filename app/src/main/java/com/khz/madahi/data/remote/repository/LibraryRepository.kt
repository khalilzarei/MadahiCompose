// data/remote/repository/LibraryRepository.kt
package com.khz.madahi.data.remote.repository

import com.khz.madahi.data.remote.api.APIService
import com.khz.madahi.helper.extention.logD
import com.khz.madahi.helper.extention.logE
import com.khz.madahi.models.Content
import com.khz.madahi.models.response.LibraryCategoriesResponse
import com.khz.madahi.models.response.LibraryContentsResponse
import com.khz.madahi.utils.Result

class LibraryRepository(
    private val apiService: APIService
) {

    // ============ دسته‌های کتابچه (جستجو + صفحه‌بندی) ============
    suspend fun getCategories(
        q: String,
        page: Int,
        limit: Int
    ): Result<LibraryCategoriesResponse> {
        return runCatching {
            val response = apiService.getLibraryCategories(
                q,
                page,
                limit
            )
            logD("getLibraryCategories: q=$q page=$page total=${response.total}")

            if (response.error) {
                return Result.Error(
                    response.errorMsg
                            ?: "خطا در دریافت دسته‌ها"
                )
            }

            Result.Success(response)
        }.getOrElse { e ->
            logE("getLibraryCategories error $e")
            Result.Error("خطا در ارتباط با سرور: ${e.message}")
        }
    }

    // ============ شعرهای یک دسته (صفحه‌بندی) ============
    suspend fun getContents(
        categoryId: Int,
        page: Int,
        limit: Int
    ): Result<LibraryContentsResponse> {
        return runCatching {
            val response = apiService.getLibraryContents(
                categoryId,
                page,
                limit
            )
            logD("getLibraryContents: categoryId=$categoryId page=$page total=${response.total}")

            if (response.error) {
                return Result.Error(
                    response.errorMsg
                            ?: "خطا در دریافت اشعار"
                )
            }

            Result.Success(response)
        }.getOrElse { e ->
            logE("getLibraryContents error $e")
            Result.Error("خطا در ارتباط با سرور: ${e.message}")
        }
    }

    // ============ جزئیات یک شعر ============
    suspend fun getContentById(contentId: Int): Result<Content> {
        return runCatching {
            val response = apiService.getContentWithId(contentId)

            if (response.error) {
                return Result.Error(
                    response.errorMsg
                            ?: "محتوا یافت نشد"
                )
            }

            val content = response.content
                    ?: return Result.Error("پاسخ سرور ناقص است")

            Result.Success(content)
        }.getOrElse { e ->
            logE("getContentWithId error $e")
            Result.Error("خطا در ارتباط با سرور: ${e.message}")
        }
    }
}
