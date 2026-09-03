// data/remote/api/APIService.kt
package com.khz.madahi.data.remote.api

import com.khz.madahi.models.response.ActivatePremiumResponse
import com.khz.madahi.models.response.AppInfoResponse
import com.khz.madahi.models.response.CategoriesResponse
import com.khz.madahi.models.response.CategoryResponse
import com.khz.madahi.models.response.ContentResponse
import com.khz.madahi.models.response.CreatePremiumInvoiceResponse
import com.khz.madahi.models.response.DataResponse
import com.khz.madahi.models.response.InsertFavoriteResponse
import com.khz.madahi.models.response.LibraryCategoriesResponse
import com.khz.madahi.models.response.LibraryContentResponse
import com.khz.madahi.models.response.LibraryContentsResponse
import com.khz.madahi.models.response.LoginResponse
import com.khz.madahi.models.response.MessageResponse
import com.khz.madahi.models.response.PremiumStatusResponse
import com.khz.madahi.models.response.ResultResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

// data/remote/api/APIService.kt
// ✅ هماهنگ با سرور امن (new_api):
//   - همه اندپوینت‌ها (به‌جز login/register/getAppInfo) توکن می‌خواهند
//     که AuthInterceptor خودکار اضافه می‌کند
//   - user_id داخل بدنه توسط سرور نادیده گرفته می‌شود (از توکن خوانده می‌شود)
//     ولی برای سازگاری کد فعلی اپ نگه داشته شده است
interface APIService {

    // ============ App Info (بدون توکن) ============
    @GET("getAppInfo.php")
    suspend fun appInfo(): AppInfoResponse

    // ============ Auth (بدون توکن) ============
    @FormUrlEncoded
    @POST("login.php")
    suspend fun login(
        @Field("data") mobile: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register.php")
    suspend fun register(
        @Field("data") mobile: String,
        @Field("full_name") fullName: String
    ): LoginResponse

    // ============ Categories (با توکن) ============
    // سرور امن از هدر Authorization توکن را می‌خواند؛
    // user_id برای سازگاری با کد قبلی ارسال می‌شود ولی نادیده گرفته می‌شود
    @FormUrlEncoded
    @POST("getUserCategories.php")
    suspend fun getCategories(
        @Field("user_id") userId: Int
    ): CategoriesResponse

    @FormUrlEncoded
    @POST("insertCategory.php")
    suspend fun insertCategory(
        @Field("user_id") userId: Int,
        @Field("title") title: String,
        @Field("description") description: String
    ): CategoryResponse

    @FormUrlEncoded
    @POST("updateCategory.php")
    suspend fun updateCategory(
        @Field("user_id") userId: Int,
        @Field("category_id") categoryId: Int,
        @Field("title") title: String,
        @Field("description") description: String
    ): CategoryResponse

    @FormUrlEncoded
    @POST("deleteCategory.php")
    suspend fun deleteCategory(
        @Field("user_id") userId: Int,
        @Field("category_id") categoryId: Int
    ): ResultResponse

    // ============ Contents (با توکن) ============
    @FormUrlEncoded
    @POST("getContentWithCategory.php")
    suspend fun getContentWithCategory(
        @Field("category_id") categoryId: Int,
        @Field("user_id") userId: Int
    ): DataResponse

    @FormUrlEncoded
    @POST("insertContent.php")
    suspend fun insertContent(
        @Field("user_id") userId: Int,
        @Field("category_id") categoryId: Int,
        @Field("answer") answer: String,
        @Field("content") content: String,
        @Field("subject") subject: String,
        @Field("content_type") contentType: String
    ): ContentResponse

    @FormUrlEncoded
    @POST("updateContent.php")
    suspend fun updateContent(
        @Field("user_id") userId: Int,
        @Field("content_id") contentId: Int,
        @Field("answer") answer: String,
        @Field("content") content: String,
        @Field("subject") subject: String,
    ): ContentResponse

    @FormUrlEncoded
    @POST("deleteContent.php")
    suspend fun deleteContent(
        @Field("user_id") userId: Int,        // ✅ اضافه شد (سرور امن لازم دارد)
        @Field("content_id") contentId: Int
    ): ResultResponse

    // ============ Favorites (با توکن) ============
    @FormUrlEncoded
    @POST("insertFavorite.php")
    suspend fun insertFavorite(
        @Field("user_id") userId: Int,
        @Field("content_id") contentId: Int
    ): InsertFavoriteResponse

    @FormUrlEncoded
    @POST("insertFavorite.php")
    suspend fun toggleFavorite(
        @Field("content_id") contentId: Int,
        @Field("user_id") userId: Int
    ): InsertFavoriteResponse

    @FormUrlEncoded
    @POST("getUserFavorites.php")
    suspend fun getUserFavorites(
        @Field("user_id") userId: Int
    ): DataResponse

    // ============ Messages (با توکن) ============
    @FormUrlEncoded
    @POST("getMessages.php")
    suspend fun getMessages(
        @Field("user_id") userId: Int
    ): MessageResponse

    // ============ Audio (با توکن) — سبک/ویس ============
    @Multipart
    @POST("uploadAudio.php")
    suspend fun uploadAudio(
        @Part("user_id") userId: RequestBody,
        @Part("content_id") contentId: RequestBody,
        // محدوده‌ی کات (ثانیه) — سرور برش را انجام می‌دهد
        // -1 = بدون برش (فایل کامل ارسال می‌شود)
        @Part("start_sec") startSec: RequestBody,
        @Part("duration_sec") durationSec: RequestBody,
        @Part audio: MultipartBody.Part
    ): ContentResponse

    // ============ Premium (نسخه پرو) ============
    @FormUrlEncoded
    @POST("getPremiumStatus.php")
    suspend fun getPremiumStatus(@Field("user_id") userId: Int): PremiumStatusResponse

    @FormUrlEncoded
    @POST("activatePremiumBazaar.php")
    suspend fun activatePremiumBazaar(
        @Field("purchase_token") purchaseToken: String,
        @Field("product_id") productId: String
    ): ActivatePremiumResponse

    @FormUrlEncoded
    @POST("createPremiumInvoice.php")
    suspend fun createPremiumInvoice(
        @Field("user_id") userId: Int
    ): CreatePremiumInvoiceResponse

    // ============ شعرهای دارای سبک (نسخه پرو) ============
    @FormUrlEncoded
    @POST("getPoemsWithAudio.php")
    suspend fun getPoemsWithAudio(
        @Field("user_id") userId: Int
    ): DataResponse

    // ============ کتابچه (کتابخانه عمومی) ============
    // دسته‌های گروه‌بندی‌شده — id هر آیتم «نماینده‌ی گروه» است
    @FormUrlEncoded
    @POST("getLibraryCategories.php")
    suspend fun getLibraryCategories(
        @Field("q") q: String = "",
        @Field("page") page: Int = 1,
        @Field("limit") limit: Int = 30
    ): LibraryCategoriesResponse

    // شعرهای یک گروه کتابچه — به‌همراه publisher_name و style
    @FormUrlEncoded
    @POST("getLibraryContents.php")
    suspend fun getLibraryContents(
        @Field("category_id") categoryId: Int,
        @Field("q") q: String = "",
        @Field("page") page: Int = 1,
        @Field("limit") limit: Int = 30
    ): LibraryContentsResponse

    // جزئیات یک شعر کتابچه — به‌همراه publisher_name و style
    @FormUrlEncoded
    @POST("getContentWithId.php")
    suspend fun getLibraryContentWithId(
        @Field("content_id") contentId: Int
    ): LibraryContentResponse
}
