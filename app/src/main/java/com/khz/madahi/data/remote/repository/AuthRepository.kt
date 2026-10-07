package com.khz.madahi.data.remote.repository

import com.khz.madahi.data.remote.api.APIService
import com.khz.madahi.helper.extention.logD
import com.khz.madahi.helper.extention.logE
import com.khz.madahi.models.response.ChangePasswordResponse
import com.khz.madahi.models.response.LoginResponse
import com.khz.madahi.utils.Result

class AuthRepository(
    private val apiService: APIService
) {

    suspend fun login(
        mobile: String,
        password: String
    ): Result<LoginResponse> {

        return runCatching {

            val response = apiService.login(
                mobile,
                password
            )

            // ⚠️ رمز عبور عمداً لاگ نمی‌شود
            logD("login response error=${response.error}, mustChangePassword=${response.mustChangePassword}")

            if (response.error) {

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

    /**
     * تغییر رمز عبور کاربر واردشده.
     * برای کاربرانی که تازه وارد شده‌اند، [currentPassword] همان شمارهٔ موبایل است.
     */
    suspend fun changePassword(
        currentPassword: String,
        newPassword: String
    ): Result<ChangePasswordResponse> {

        return runCatching {

            val response = apiService.changePassword(
                currentPassword,
                newPassword
            )

            // ⚠️ رمز عبور عمداً لاگ نمی‌شود
            logD("changePassword response error=${response.error}")

            if (response.error == true) {
                return Result.Error(
                    response.errorMsg
                            ?: "تغییر رمز عبور ناموفق بود"
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