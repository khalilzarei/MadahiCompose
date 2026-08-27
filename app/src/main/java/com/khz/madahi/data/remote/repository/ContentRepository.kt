// data/remote/repository/ContentRepository.kt
package com.khz.madahi.data.remote.repository

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.khz.madahi.data.local.database.dao.ContentDAO
import com.khz.madahi.data.local.database.dao.FavoriteDAO
import com.khz.madahi.data.remote.api.APIService
import com.khz.madahi.models.Content
import com.khz.madahi.models.Favorite
import com.khz.madahi.utils.Result
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class ContentRepository(
    private val apiService: APIService,
    private val contentDao: ContentDAO,
    private val favoriteDao: FavoriteDAO
) {

    companion object {
        private const val TAG = "ContentRepository"
    }

    // ============ Get Contents ============
    suspend fun getContents(
        categoryId: Int,
        userId: Int
    ): Result<List<Content>> {
        return try {
            Log.d(
                TAG,
                "getContents: categoryId=$categoryId, userId=$userId"
            )

            val response = apiService.getContentWithCategory(
                categoryId,
                userId
            )

            Log.d(
                TAG,
                "getContents: response=$response"
            )

            if (response.error) {
                val errorMsg = response.errorMsg
                        ?: "خطا در دریافت محتواها"
                Log.e(
                    TAG,
                    "getContents: server error=$errorMsg"
                )
                return Result.Error(errorMsg)
            }

            val contents = response.contents?.filterNotNull()
                    ?: emptyList()
            Log.d(
                TAG,
                "getContents: contents size=${contents.size}"
            )

            if (contents.isNotEmpty()) {
                contentDao.deleteByCategoryId(categoryId)
                contentDao.insertAll(contents)
            }

            Result.Success(contents)

        } catch (e: Exception) {
            Log.e(
                TAG,
                "getContents error",
                e
            )

            val cached = contentDao.getByCategoryId(categoryId)
            if (cached.isNotEmpty()) {
                Log.d(
                    TAG,
                    "getContents: using cached data, size=${cached.size}"
                )
                return Result.Success(cached)
            }

            Result.Error("خطا در ارتباط با سرور: ${e.message}")
        }
    }

    // ============ Add Content ============
    suspend fun addContent(content: Content): Result<Content> {
        return try {
            Log.d(
                TAG,
                "addContent: userId=${content.userId}, categoryId=${content.categoryId}, subject=${content.subject}"
            )

            val response = apiService.insertContent(
                content.userId,
                content.categoryId,
                content.answer,
                content.content,
                content.subject,
                content.contentType
            )

            Log.d(
                TAG,
                "addContent: response=$response"
            )

            if (response.error) {
                val errorMsg = response.errorMsg
                        ?: "خطا در افزودن محتوا"
                Log.e(
                    TAG,
                    "addContent: server error=$errorMsg"
                )
                return Result.Error(errorMsg)
            }

            val newContent = response.content
            if (newContent == null) {
                Log.e(
                    TAG,
                    "addContent: content is null"
                )
                return Result.Error("محتوا ایجاد نشد")
            }

            contentDao.insert(newContent)
            Log.d(
                TAG,
                "addContent: success ✅"
            )

            Result.Success(newContent)

        } catch (e: Exception) {
            Log.e(
                TAG,
                "addContent error",
                e
            )
            Result.Error("خطا در ارتباط با سرور: ${e.message}")
        }
    }

    // ============ Update Content ============
    suspend fun updateContent(content: Content): Result<Content> {
        return try {
            Log.d(
                TAG,
                "updateContent: userId=${content.userId}, contentId=${content.id}"
            )

            val response = apiService.updateContent(
                content.userId,
                content.id,
                content.answer,
                content.content,
                content.subject
            )

            Log.d(
                TAG,
                "updateContent: response=$response"
            )

            if (response.error) {
                val errorMsg = response.errorMsg
                        ?: "خطا در ویرایش محتوا"
                Log.e(
                    TAG,
                    "updateContent: server error=$errorMsg"
                )
                return Result.Error(errorMsg)
            }

            contentDao.update(content)
            Log.d(
                TAG,
                "updateContent: success ✅"
            )

            Result.Success(content)

        } catch (e: Exception) {
            Log.e(
                TAG,
                "updateContent error",
                e
            )
            Result.Error("خطا در ارتباط با سرور: ${e.message}")
        }
    }

    // ============ Delete Content ============
    suspend fun deleteContent(
        userId: Int,
        contentId: Int
    ): Result<Boolean> {
        return try {
            Log.d(
                TAG,
                "deleteContent: contentId=$contentId"
            )

            val response = apiService.deleteContent(
                userId,
                contentId
            )

            Log.d(
                TAG,
                "deleteContent: response=$response"
            )

            if (!response.success) {
                val errorMsg = response.message
                        ?: "خطا در حذف محتوا"
                Log.e(
                    TAG,
                    "deleteContent: server error=$errorMsg"
                )
                return Result.Error(errorMsg)
            }

            contentDao.deleteById(contentId)
            Log.d(
                TAG,
                "deleteContent: success ✅"
            )

            Result.Success(true)

        } catch (e: Exception) {
            Log.e(
                TAG,
                "deleteContent error",
                e
            )
            Result.Error("خطا در ارتباط با سرور: ${e.message}")
        }
    }

    // ============ Add Favorite (suspend - بدون Call) ============
    suspend fun addFavorite(
        userId: Int,
        contentId: Int
    ): Result<Favorite> {
        return try {
            Log.d(
                TAG,
                "addFavorite: userId=$userId, contentId=$contentId"
            )

            // ✅ suspend - مستقیماً InsertFavoriteResponse برمی‌گرداند
            val response = apiService.insertFavorite(
                userId,
                contentId
            )

            Log.d(
                TAG,
                "addFavorite: response=$response"
            )

            if (response.error == true) {
                val errorMsg = response.errorMsg
                        ?: "خطا در افزودن به علاقه‌مندی‌ها"
                Log.e(
                    TAG,
                    "addFavorite: server error=$errorMsg"
                )
                return Result.Error(errorMsg)
            }

            val favorite = response.favorite
            if (favorite == null) {
                Log.e(
                    TAG,
                    "addFavorite: favorite is null"
                )
                return Result.Error("علاقه‌مندی ایجاد نشد")
            }

            Log.d(
                TAG,
                "addFavorite: success ✅"
            )
            Result.Success(favorite)

        } catch (e: Exception) {
            Log.e(
                TAG,
                "addFavorite error",
                e
            )
            Result.Error("خطا در ارتباط با سرور: ${e.message}")
        }
    }

    // ============ Remove Favorite ============
    suspend fun removeFavorite(
        userId: Int,
        contentId: Int
    ): Result<Boolean> {
        return try {
            Log.d(
                TAG,
                "removeFavorite: userId=$userId, contentId=$contentId"
            )

            // ✅ از همان API insertFavorite استفاده می‌کنیم
            // سرور خودش تشخیص می‌دهد که اگر وجود داشته باشد حذف کند
            val response = apiService.insertFavorite(
                userId,
                contentId
            )

            Log.d(
                TAG,
                "removeFavorite: response=$response"
            )

            if (response.error == true) {
                val errorMsg = response.errorMsg
                        ?: "خطا در حذف از علاقه‌مندی‌ها"
                Log.e(
                    TAG,
                    "removeFavorite: server error=$errorMsg"
                )
                return Result.Error(errorMsg)
            }

            // ✅ اگر favorite برگردانده شد یعنی اضافه شده، اما ما می‌خواهیم حذف کنیم
            // پس اگر favorite null بود یعنی حذف شده است
            val favorite = response.favorite
            if (favorite == null) {
                Log.d(
                    TAG,
                    "removeFavorite: successfully removed from server ✅"
                )
            } else {
                Log.d(
                    TAG,
                    "removeFavorite: favorite returned from server (maybe added again?)"
                )
            }

            // ✅ حذف از دیتابیس محلی
            favoriteDao.deleteByContentId(contentId)
            Log.d(
                TAG,
                "removeFavorite: removed from local DB ✅"
            )

            Result.Success(true)

        } catch (e: Exception) {
            Log.e(
                TAG,
                "removeFavorite error",
                e
            )
            Result.Error("خطا در ارتباط با سرور: ${e.message}")
        }
    }

    // data/remote/repository/ContentRepository.kt

    // ============ Toggle Favorite (اضافه/حذف) ============
    suspend fun toggleFavorite(
        userId: Int,
        contentId: Int
    ): Result<Favorite?> {
        return try {
            Log.d(
                TAG,
                "toggleFavorite: userId=$userId, contentId=$contentId"
            )

            val response = apiService.toggleFavorite(
                contentId,
                userId
            )

            Log.d(
                TAG,
                "toggleFavorite: response=$response"
            )
            Log.d(
                TAG,
                "toggleFavorite: action=${response.action}"
            )
            Log.d(
                TAG,
                "toggleFavorite: favorite=${response.favorite}"
            )

            if (response.error == true) {
                val errorMsg = response.errorMsg
                        ?: "خطا در انجام عملیات"
                Log.e(
                    TAG,
                    "toggleFavorite: server error=$errorMsg"
                )
                return Result.Error(errorMsg)
            }

            val favorite = response.favorite
            val action = response.action

            // ✅ بر اساس action تصمیم بگیر
            when (action) {
                "added" -> {
                    // ✅ اضافه شده
                    Log.d(
                        TAG,
                        "toggleFavorite: added ✅"
                    )
                    if (favorite != null) {
                        favoriteDao.insert(favorite)
                    }
                    Result.Success(favorite)
                }

                "removed" -> {
                    // ✅ حذف شده
                    Log.d(
                        TAG,
                        "toggleFavorite: removed ✅"
                    )
                    favoriteDao.deleteByContentId(contentId)
                    Result.Success(null)  // ✅ null برگردان یعنی حذف شده
                }

                else -> {
                    // ✅ اگر action مشخص نبود، بر اساس favorite تصمیم بگیر
                    if (favorite != null) {
                        favoriteDao.insert(favorite)
                        Result.Success(favorite)
                    } else {
                        favoriteDao.deleteByContentId(contentId)
                        Result.Success(null)
                    }
                }
            }

        } catch (e: Exception) {
            Log.e(
                TAG,
                "toggleFavorite error",
                e
            )
            Result.Error("خطا در ارتباط با سرور: ${e.message}")
        }
    }

    // ============ آپلود ویس/سبک (نسخه پرو) ============
    suspend fun uploadAudio(
        context: Context,
        userId: Int,
        content: Content,
        uri: Uri
    ): Result<Content> {
        return try {
            Log.d(
                TAG,
                "uploadAudio: contentId=${content.id}"
            )

            val fileName = queryFileName(
                context.contentResolver,
                uri
            )
                    ?: "audio.mp3"
            val mimeType = context.contentResolver.getType(uri)
                    ?: "audio/mp3"

            val bytes = context.contentResolver.openInputStream(uri)
                ?.use { it.readBytes() }
                    ?: return Result.Error("خواندن فایل ممکن نبود")

            if (bytes.isEmpty()) {
                return Result.Error("فایل خالی است")
            }

            val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData(
                "audio",
                fileName,
                requestBody
            )

            val response = apiService.uploadAudio(
                userId.toString()
                    .toRequestBody(),
                content.id.toString()
                    .toRequestBody(),
                part
            )

            Log.d(
                TAG,
                "uploadAudio: response error=${response.error}"
            )

            if (response.error) {
                val errorMsg = response.errorMsg
                        ?: "خطا در آپلود ویس"
                Log.e(
                    TAG,
                    "uploadAudio: server error=$errorMsg"
                )
                return Result.Error(errorMsg)
            }

            val uploaded = response.content
                    ?: return Result.Error("پاسخ سرور ناقص است")

            Result.Success(uploaded)

        } catch (e: Exception) {
            Log.e(
                TAG,
                "uploadAudio error",
                e
            )
            Result.Error("خطا در ارتباط با سرور: ${e.message}")
        }
    }

    /** دریافت نام فایل از Uri (بدون permission اضافی) */
    private fun queryFileName(
        resolver: ContentResolver,
        uri: Uri
    ): String? {
        var name: String? = null
        resolver.query(
            uri,
            null,
            null,
            null
        )
            ?.use { cursor ->
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index >= 0 && cursor.moveToFirst()) {
                    name = cursor.getString(index)
                }
            }
        return name
    }
}