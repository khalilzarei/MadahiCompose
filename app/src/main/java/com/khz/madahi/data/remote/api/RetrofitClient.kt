// data/remote/api/RetrofitClient.kt
package com.khz.madahi.data.remote.api

import com.google.gson.GsonBuilder
import com.khz.madahi.BuildConfig
import com.khz.madahi.helper.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * ✅ بهینه‌سازی نسبت به نسخه‌ی قبلی:
 *  - قبلاً `client` هر بار که apiService خوانده می‌شد از نو ساخته می‌شد
 *    (چون داخل getter بدون cache بود)، در حالی که فقط `retrofit` کش می‌شد.
 *    این باعث ساخته‌شدن یک OkHttpClient جدید (با connection pool و
 *    thread pool مخصوص خودش) در هر فراخوانی می‌شد که هم اتلاف منابع بود
 *    و هم عملاً استفاده نمی‌شد چون retrofit از قبل ساخته شده بود.
 *  - الان هم `client` و هم `retrofit` به‌صورت `by lazy` فقط یک بار ساخته
 *    می‌شوند (Thread-safe به‌صورت پیش‌فرض در Kotlin).
 *  - کدهای مرده و کامنت‌شده‌ی نسخه‌ی قبلی حذف شدند.
 */
object RetrofitClient {

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(
                2,
                TimeUnit.MINUTES
            )
            .readTimeout(
                2,
                TimeUnit.MINUTES
            )
            .writeTimeout(
                2,
                TimeUnit.MINUTES
            )
            .retryOnConnectionFailure(true)
            .addInterceptor { chain: Interceptor.Chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .method(
                        original.method,
                        original.body
                    )
                    .build()
                chain.proceed(request)
            }
            .apply {
                // ✅ لاگ فقط در نسخه‌ی Debug فعال است تا در ریلیز نشتی اطلاعات نداشته باشیم
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

    // ✅ apiService هم فقط یک بار ساخته و کش می‌شود
    val apiService: APIService by lazy {
        retrofit.create(APIService::class.java)
    }
}