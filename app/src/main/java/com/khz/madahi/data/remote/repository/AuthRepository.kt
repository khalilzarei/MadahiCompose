package com.khz.madahi.data.remote.repository

import com.khz.madahi.data.remote.api.APIService
import com.khz.madahi.helper.extention.logD
import com.khz.madahi.helper.extention.logE
import com.khz.madahi.models.response.LoginResponse
import com.khz.madahi.utils.Result

class AuthRepository(
    private val apiService: APIService
) {

    suspend fun login(mobile: String): Result<LoginResponse> {

        return runCatching {

            val response = apiService.login(mobile)

            logD("login response $response")

            if (response.error == true) {

                return Result.Error(
                    response.errorMsg
                            ?: "خطا"
                )

            }

            Result.Success(response)

        }.getOrElse {

            Result.Error(
                it.message
                        ?: "Unknown Error"
            )

        }

    }

    // AuthRepository.kt - register با suspend
    suspend fun register(
        mobile: String,
        fullName: String
    ): Result<LoginResponse> {
        return runCatching {
            logD("register: mobile=$mobile, fullName=$fullName")

            val response = apiService.register(
                mobile,
                fullName
            )
            logD("register: response=$response")

            if (response.error == true) {
                val errorMsg = response.errorMsg
                        ?: "خطا در ثبت نام"
                logE("register: server error=$errorMsg")
                return Result.Error(errorMsg)
            }

            if (response.user == null) {
                logE("register: user is null")
                return Result.Error("اطلاعات کاربر یافت نشد")
            }

            logD("register: success ✅")
            Result.Success(response)

        }.getOrElse { e ->
            logE("register: exception=${e.message}")
            Result.Error(
                e.message
                        ?: "Unknown Error"
            )
        }
    }
}