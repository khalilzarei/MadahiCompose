// data/remote/repository/AppInfoRepository.kt
package com.khz.madahi.data.remote.repository

import com.khz.madahi.data.remote.api.APIService
import com.khz.madahi.helper.extention.logD
import com.khz.madahi.helper.extention.logE
import com.khz.madahi.models.AppInfo
import com.khz.madahi.utils.Result

class AppInfoRepository(
    private val apiService: APIService
) {

    suspend fun getAppInfo(): Result<AppInfo> {
        return runCatching {
            val response = apiService.appInfo()
            logD(
                "getAppInfo response: $response"
            )

            val error = response.error
            if (error == true) {
                val errorMsg = response.errorMsg
                        ?: "خطا در دریافت اطلاعات"
                return Result.Error(errorMsg)
            }

            val appInfo = response.appInfo
                    ?: return Result.Error("اطلاعات نسخه یافت نشد")

            Result.Success(appInfo)
        }.getOrElse { e ->
            logE(
                "getAppInfo error $e"
            )
            Result.Error("خطا در ارتباط با سرور: ${e.message}")
        }
    }
}