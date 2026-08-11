package com.khz.madahi.data.remote.api

import com.khz.madahi.models.response.AppInfoResponse
import com.khz.madahi.models.response.CategoriesResponse
import com.khz.madahi.models.response.CategoryResponse
import com.khz.madahi.models.response.ContentResponse
import com.khz.madahi.models.response.DataResponse
import com.khz.madahi.models.response.InsertFavoriteResponse
import com.khz.madahi.models.response.LoginResponse
import com.khz.madahi.models.response.MessageResponse
import com.khz.madahi.models.response.ResultResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// data/remote/api/APIService.kt
interface APIService {

    // ============ App Info ============
    @GET("getAppInfo.php")
    suspend fun appInfo(): AppInfoResponse

    // ============ Auth ============
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

    // ============ Categories ============
    @GET("getUserCategories.php")
    suspend fun getCategories(
        @Query("user_id") userId: String
    ): CategoriesResponse

    @FormUrlEncoded
    @POST("insertCategory.php")
    suspend fun insertCategory(
        @Field("user_id") userId: String,
        @Field("msg") title: String,
        @Field("description") description: String
    ): CategoryResponse

    @FormUrlEncoded
    @POST("updateCategory.php")
    suspend fun updateCategory(
        @Field("user_id") userId: String,
        @Field("category_id") categoryId: String,
        @Field("title") title: String,
        @Field("description") description: String
    ): CategoryResponse

    @FormUrlEncoded
    @POST("deleteCategory.php")
    suspend fun deleteCategory(
        @Field("user_id") userId: String,       // ✅ اضافه شد
        @Field("category_id") categoryId: String
    ): ResultResponse

    // ============ Contents ============
    @FormUrlEncoded
    @POST("getContentWithCategory.php")
    suspend fun getContentWithCategory(
        @Field("category_id") categoryId: String,
        @Field("user_id") userId: String
    ): DataResponse

    @FormUrlEncoded
    @POST("insertContent.php")
    suspend fun insertContent(
        @Field("user_id") userId: String,
        @Field("category_id") categoryId: String,
        @Field("answer") answer: String,
        @Field("content") content: String,
        @Field("subject") subject: String,
        @Field("content_type") contentType: String
    ): ContentResponse

    @FormUrlEncoded
    @POST("updateContent.php")
    suspend fun updateContent(
        @Field("user_id") userId: String,
        @Field("content_id") contentId: String,
        @Field("answer") answer: String,
        @Field("content") content: String,
        @Field("subject") subject: String
    ): ContentResponse

    @FormUrlEncoded
    @POST("deleteContent.php")
    suspend fun deleteContent(
        @Field("content_id") contentId: String
    ): ResultResponse

    // ============ Favorites ============
    @FormUrlEncoded
    @POST("insertFavorite.php")
    suspend fun insertFavorite(
        @Field("user_id") userId: String,
        @Field("content_id") contentId: String
    ): InsertFavoriteResponse

    @FormUrlEncoded
    @POST("insertFavorite.php")
    suspend fun toggleFavorite(
        @Field("content_id") contentId: String,
        @Field("user_id") userId: String
    ): InsertFavoriteResponse

    @FormUrlEncoded
    @POST("getUserFavorites.php")
    suspend fun getUserFavorites(
        @Field("user_id") userId: String
    ): DataResponse

    // ============ Messages ============
    @FormUrlEncoded
    @POST("getMessages.php")
    suspend fun getMessages(
        @Field("user_id") userId: String
    ): MessageResponse
}