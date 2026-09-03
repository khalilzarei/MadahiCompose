// data/remote/repository/BookletRepository.kt
package com.khz.madahi.data.remote.repository

import com.khz.madahi.data.remote.api.APIService
import com.khz.madahi.helper.extention.logD
import com.khz.madahi.helper.extention.logE
import com.khz.madahi.models.Category
import com.khz.madahi.models.LibraryContent
import com.khz.madahi.utils.Result

/**
 * یک صفحه از نتایج کتابچه — همراه اطلاعات صفحه‌بندی
 */
data class BookletPage<T>(
    val items: List<T>,
    val page: Int,
    val pages: Int,
    val total: Int
) {
    val hasMore: Boolean get() = page < pages
}

/**
 * ریپازیتوری «کتابچه» — فقط برای کتابخانه عمومی استفاده می‌شود
 * و هیچ ارتباطی با دفترچه (CategoryRepository / ContentRepository) ندارد.
 */
class BookletRepository(
    private val apiService: APIService
) {

    // ============ دسته‌های کتابچه (گروه‌بندی‌شده) — صفحه‌بندی ============
    suspend fun getCategories(
        q: String = "",
        page: Int = 1,
        limit: Int = 30
    ): Result<BookletPage<Category>> {
        return runCatching {
            val response = apiService.getLibraryCategories(
                q = q,
                page = page,
                limit = limit
            )

            if (response.error) {
                return Result.Error(
                    response.errorMsg
                            ?: "خطا در دریافت دسته‌های کتابچه"
                )
            }

            val categories = response.categories?.filterNotNull()
                    ?: emptyList()

            logD("BookletRepository: categories page=${response.page}/${response.pages}, total=${response.total}, loaded=${categories.size}")
            Result.Success(
                BookletPage(
                    items = categories,
                    page = response.page,
                    pages = response.pages,
                    total = response.total
                )
            )

        }.getOrElse { e ->
            logE("BookletRepository.getCategories error: $e")
            Result.Error("خطا در ارتباط با سرور: ${e.message}")
        }
    }

    // ============ شعرهای یک گروه کتابچه — صفحه‌بندی ============
    suspend fun getContents(
        categoryId: Int,
        q: String = "",
        page: Int = 1,
        limit: Int = 30
    ): Result<BookletPage<LibraryContent>> {
        return runCatching {
            val response = apiService.getLibraryContents(
                categoryId = categoryId,
                q = q,
                page = page,
                limit = limit
            )

            if (response.error) {
                return Result.Error(
                    response.errorMsg
                            ?: "خطا در دریافت شعرهای کتابچه"
                )
            }

            val contents = response.contents?.filterNotNull()
                    ?: emptyList()

            logD("BookletRepository: contents group=$categoryId page=${response.page}/${response.pages}, total=${response.total}, loaded=${contents.size}")
            Result.Success(
                BookletPage(
                    items = contents,
                    page = response.page,
                    pages = response.pages,
                    total = response.total
                )
            )

        }.getOrElse { e ->
            logE("BookletRepository.getContents error: $e")
            Result.Error("خطا در ارتباط با سرور: ${e.message}")
        }
    }

    // ============ جزئیات یک شعر کتابچه ============
    suspend fun getContent(contentId: Int): Result<LibraryContent> {
        return runCatching {
            val response = apiService.getLibraryContentWithId(contentId)

            if (response.error) {
                return Result.Error(
                    response.errorMsg
                            ?: "خطا در دریافت شعر"
                )
            }

            val content = response.content
                    ?: return Result.Error("شعر یافت نشد")

            Result.Success(content)

        }.getOrElse { e ->
            logE("BookletRepository.getContent error: $e")
            Result.Error("خطا در ارتباط با سرور: ${e.message}")
        }
    }
}
