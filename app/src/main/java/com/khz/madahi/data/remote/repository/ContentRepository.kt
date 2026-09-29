package com.khz.madahi.data.remote.repository

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
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class ContentRepository(
    private val apiService: APIService,
    private val contentDao: ContentDAO,
    private val favoriteDao: FavoriteDAO
) {

    suspend fun removeAudio(contentId: Int): Result<Unit> = try {
        val response = apiService.removeAudio(contentId)
        if (response.error) Result.Error(
            response.errorMsg
                    ?: "حذف ویس ناموفق بود"
        )
        else Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error("خطا در حذف ویس: ${e.message}")
    }

    companion object {
        private const val TAG = "ContentRepository"
    }

    private fun createTextPart(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }

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

    suspend fun getFavorites(userId: Int): Result<List<Content>> {
        return try {
            Log.d(
                TAG,
                "getFavorites: userId=$userId"
            )

            val response = apiService.getUserFavorites(userId)

            if (response.error) {
                return Result.Error(
                    response.errorMsg
                            ?: "خطا در دریافت علاقه‌مندی‌ها"
                )
            }

            Result.Success(
                response.contents?.filterNotNull()
                        ?: emptyList()
            )

        } catch (e: Exception) {
            Log.e(
                TAG,
                "getFavorites error",
                e
            )
            Result.Error("خطا در ارتباط با سرور: ${e.message}")
        }
    }

    suspend fun addFavorite(
        userId: Int,
        contentId: Int
    ): Result<Favorite> {
        return try {
            Log.d(
                TAG,
                "addFavorite: userId=$userId, contentId=$contentId"
            )

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

    suspend fun removeFavorite(
        userId: Int,
        contentId: Int
    ): Result<Boolean> {
        return try {
            Log.d(
                TAG,
                "removeFavorite: userId=$userId, contentId=$contentId"
            )

            // ✅ باگ قبلی: از insertFavorite() استفاده می‌شد که در واقع
            //    فیوریت را دوباره اضافه می‌کرد! الان از toggleFavorite
            //    (همان اندپوینت سرور ولی با پارامترهای درست) استفاده می‌شود
            val response = apiService.toggleFavorite(
                contentId,
                userId
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

            when (response.action) {
                "removed" -> {
                    Log.d(
                        TAG,
                        "removeFavorite: removed from server ✅"
                    )
                }

                "added"   -> {
                    // سرور فیوریت را مجدداً اضافه کرد (ممکن است سرور toggle کند)
                    Log.w(
                        TAG,
                        "removeFavorite: server re-added favorite, removing from local DB anyway"
                    )
                }

                else      -> {
                    Log.d(
                        TAG,
                        "removeFavorite: action=${response.action}"
                    )
                }
            }

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

            when (action) {
                "added"   -> {
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
                    Log.d(
                        TAG,
                        "toggleFavorite: removed ✅"
                    )
                    favoriteDao.deleteByContentId(contentId)
                    Result.Success(null)
                }

                else      -> {
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

    suspend fun uploadAudio(
        context: Context,
        userId: Int,
        content: Content,
        uri: Uri,
        startSec: Double = -1.0,
        durationSec: Double = -1.0
    ): Result<Content> {
        return try {
            Log.d(
                TAG,
                "uploadAudio: contentId=${content.id}"
            )

            // ۱) نام فایل واقعی (برای فایل ضبطشده، از path استخراج میشود)
            val fileName = resolveFileName(
                context,
                uri
            )
                    ?: "audio.m4a"

            // ۲) تشخیص MIME بر اساس نام فایل
            val mimeType = context.contentResolver.getType(uri)
                    ?: mimeFromExtension(fileName)

            val bytes = context.contentResolver.openInputStream(uri)
                ?.use { it.readBytes() }
                    ?: return Result.Error("خواندن فایل ممکن نبود")

            Log.d(
                TAG,
                "uploadAudio: fileName=$fileName bytes=${bytes.size} mime=$mimeType"
            )

            if (bytes.isEmpty()) {
                return Result.Error("فایل خالی است")
            }

            val requestFile = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
            val audioPart = MultipartBody.Part.createFormData(
                "audio",
                fileName,
                requestFile
            )

            val response = apiService.uploadAudio(
                userId = createTextPart(userId.toString()),
                contentId = createTextPart(content.id.toString()),
                startSec = createTextPart("-1"),
                durationSec = createTextPart("-1"),
                audio = audioPart
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

    /**
     * نام صحیح فایل را برمیگرداند:
     * - برای فایلهای انتخابی از گالری → OpenableColumns
     * - برای فایل ضبطشده (file://) → آخرین بخش مسیر
     */
    private fun resolveFileName(
        context: Context,
        uri: Uri
    ): String? {
        // اول از ContentResolver
        try {
            context.contentResolver.query(
                uri,
                null,
                null,
                null,
                null
            )
                ?.use { cursor ->
                    val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (index >= 0 && cursor.moveToFirst()) {
                        val name = cursor.getString(index)
                        if (!name.isNullOrBlank()) return name
                    }
                }
        } catch (_: Exception) {
        }

        // fallback: از خود URI
        return uri.lastPathSegment?.substringAfterLast('/')
                ?: null
    }

    /**
     * بررسی لینک فایل بهصورت غیرمقرونبهصرفه:
     * - اول HEAD میفرستیم (بدون Range)
     * - بعد GET با Range کوچک
     * - هرگز 412 را خطای بحرانی نمیگیریم چون ممکن است فقط محدودیت سرور باشد.
     */
    suspend fun verifyAudioUrl(url: String): Result<String> {
        return kotlinx.coroutines.withContext(Dispatchers.IO) {
            try {
                val client = com.khz.madahi.data.remote.api.RetrofitClient.httpClient

                // ===== ۱) تلاش اول: HEAD =====
                val headRequest = okhttp3.Request.Builder()
                    .url(url)
                    .header(
                        "User-Agent",
                        "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0 Mobile Safari/537.36"
                    )
                    .head()
                    .build()

                client.newCall(headRequest)
                    .execute()
                    .use { headResponse ->
                        val headCode = headResponse.code
                        val headType = headResponse.header("Content-Type")
                                ?: "unknown"
                        headResponse.close()

                        // اگر HEAD با موفقیت جواب داد
                        if (headCode == 200 || headCode == 206 || headCode == 204 || headCode in 301..308  // ریدایرکتهای معتبر
                        ) {
                            return@withContext Result.Success("HTTP $headCode | $headType")
                        }

                        // اگر HEAD مسدود بود (403/405/412/...) ادامه بده
                    }

                // ===== ۲) تلاش دوم: GET با Range کوچک =====
                val rangeRequest = okhttp3.Request.Builder()
                    .url(url)
                    .header(
                        "Range",
                        "bytes=0-0"
                    )
                    .header(
                        "User-Agent",
                        "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0 Mobile Safari/537.36"
                    )
                    .build()

                client.newCall(rangeRequest)
                    .execute()
                    .use { rangeResponse ->
                        val rangeCode = rangeResponse.code
                        val rangeType = rangeResponse.header("Content-Type")
                                ?: "unknown"

                        // اگر 200 یا 206 شد یعنی فایل سالم است
                        if (rangeCode == 200 || rangeCode == 206) {
                            return@withContext Result.Success("HTTP $rangeCode | $rangeType")
                        }

                        // اگر 412 یا 416 شد یعنی فایل وجود دارد ولی سرور Range را قبول ندارد
                        // این وضعیت را بهعنوان هشدار در نظر میگیریم نه خطای حتمی
                        if (rangeCode == 412 || rangeCode == 416) {
                            return@withContext Result.Success("HTTP $rangeCode (بهاحتمال زیاد فایل موجود است) | $rangeType")
                        }

                        // کاملاً 404 یا مشابه؟ خطای واقعی
                        return@withContext Result.Error(
                            "لینک فایل پاسخ HTTP $rangeCode داد — فایل در آن آدرس موجود نیست ($rangeType)"
                        )
                    }

            } catch (e: Exception) {
                Result.Error("بررسی لینک ممکن نبود: ${e.message}")
            }
        }
    }

    private fun mimeFromExtension(fileName: String): String {
        val ext = fileName.substringAfterLast(
            '.',
            ""
        )
            .lowercase()
        return when (ext) {
            "mp3" -> "audio/mpeg"
            "m4a" -> "audio/m4a"
            "aac" -> "audio/aac"
            "ogg" -> "audio/ogg"
            "wav" -> "audio/wav"
            "mp4" -> "audio/mp4"
            else  -> "application/octet-stream"
        }
    }
}
