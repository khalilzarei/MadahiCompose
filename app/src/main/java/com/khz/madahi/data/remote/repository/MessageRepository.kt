// data/remote/repository/MessageRepository.kt
package com.khz.madahi.data.remote.repository

import com.khz.madahi.data.remote.api.APIService
import com.khz.madahi.helper.extention.logD
import com.khz.madahi.helper.extention.logE
import com.khz.madahi.models.MessageItem
import com.khz.madahi.utils.Result

class MessageRepository(
    private val apiService: APIService
) {

    suspend fun getMessages(userId: Int): Result<List<MessageItem>> {
        return runCatching {
            val response = apiService.getMessages(userId)
            logD(
                "getMessages response: $response"
            )

            val error = response.error
            if (error == true) {
                val errorMsg = response.errorMsg
                        ?: "خطا در دریافت پیام‌ها"
                return Result.Error(errorMsg)
            }

            val items = response.messageItems?.filterNotNull()
                    ?: emptyList()

            Result.Success(items)
        }.getOrElse { e ->
            logE(
                "getMessages error $e"
            )
            Result.Error("خطا در ارتباط با سرور: ${e.message}")
        }
    }
}
