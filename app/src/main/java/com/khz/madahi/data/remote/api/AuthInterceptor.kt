// data/remote/api/AuthInterceptor.kt
package com.khz.madahi.data.remote.api

import com.khz.madahi.helper.extention.logD
import okhttp3.Interceptor
import okhttp3.Response

/**
 * هدر Authorization را به همه درخواست‌ها اضافه می‌کند:
 * Authorization: Bearer <token>
 *
 * سرور امن (new_api) کاربر را از روی همین توکن می‌شناسد،
 * نه از user_id داخل بدنه.
 */
class AuthInterceptor(
    private val tokenProvider: () -> String?
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenProvider()
        val request = if (token.isNullOrEmpty()) {
            chain.request()
        } else {
            val header = "Bearer $token"
            // ⚠️ فقط بخشی از توکن لاگ می‌شود (در Release همه‌ی لاگ‌ها بی‌اثرند)
            logD("intercept Bearer ${token.take(6)}…")
            chain.request()
                .newBuilder()
                .header(
                    "Authorization",
                    header
                )
                .build()
        }
        return chain.proceed(request)
    }
}
