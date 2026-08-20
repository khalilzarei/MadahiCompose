// data/remote/api/RetrofitClient.kt
package com.khz.madahi.data.remote.api

import android.content.Context
import com.google.gson.GsonBuilder
import com.khz.madahi.BuildConfig
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.helper.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private lateinit var appContext: Context

    /** حتماً در App.onCreate صدا زده شود */
    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(
                20,
                TimeUnit.SECONDS
            )
            .readTimeout(
                30,
                TimeUnit.SECONDS
            )
            .writeTimeout(
                30,
                TimeUnit.SECONDS
            )
            .retryOnConnectionFailure(true)
            // ✅ افزودن خودکار هدر توکن به همه درخواست‌ها
            .addInterceptor(
                AuthInterceptor {
                    if (::appContext.isInitialized) {
                        PreferencesManager(appContext).token
                    } else {
                        null
                    }
                })
            .apply {
                // ✅ لاگ فقط در نسخه‌ی Debug
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                    addInterceptor(logging)
                }
            }
            .build()
    }

    private val retrofit: Retrofit by lazy {
        val gson = GsonBuilder().setLenient()
            .create()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // ✅ apiService فقط یک بار ساخته و کش می‌شود
    val apiService: APIService by lazy {
        retrofit.create(APIService::class.java)
    }
}
