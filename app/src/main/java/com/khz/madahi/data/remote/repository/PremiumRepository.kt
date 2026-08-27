// data/remote/repository/PremiumRepository.kt
package com.khz.madahi.data.remote.repository

import com.khz.madahi.data.remote.api.APIService
import com.khz.madahi.helper.BAZAAR_PRO_PRODUCT_ID
import com.khz.madahi.helper.extention.logD
import com.khz.madahi.helper.extention.logE
import com.khz.madahi.models.response.ActivatePremiumResponse
import com.khz.madahi.models.response.CreatePremiumInvoiceResponse
import com.khz.madahi.models.response.DataResponse
import com.khz.madahi.models.response.PremiumStatusResponse
import com.khz.madahi.utils.Result

class PremiumRepository(
    private val apiService: APIService
) {

    // ============ وضعیت پرو ============
    suspend fun getPremiumStatus(userId: Int): Result<PremiumStatusResponse> {
        return runCatching {
            val response = apiService.getPremiumStatus(userId)
            logD("getPremiumStatus: isPremium=${response.isPremium}")

            if (response.error) {
                return Result.Error(
                    response.errorMsg
                            ?: "خطا در دریافت وضعیت"
                )
            }

            Result.Success(response)
        }.getOrElse { e ->
            logE("getPremiumStatus error $e")
            Result.Error("خطا در ارتباط با سرور: ${e.message}")
        }
    }

    // ============ فعال‌سازی از طریق خرید بازار ============
    suspend fun activateFromBazaar(purchaseToken: String): Result<ActivatePremiumResponse> {
        return runCatching {
            val response = apiService.activatePremiumBazaar(
                purchaseToken = purchaseToken,
                productId = BAZAAR_PRO_PRODUCT_ID
            )
            logD("activateFromBazaar: error=${response.error} already=${response.already}")

            if (response.error) {
                return Result.Error(
                    response.errorMsg
                            ?: "خطا در فعال‌سازی"
                )
            }

            Result.Success(response)
        }.getOrElse { e ->
            logE("activateFromBazaar error $e")
            Result.Error("خطا در ارتباط با سرور: ${e.message}")
        }
    }

    // ============ ساخت فاکتور زرین‌پال (کانال دوم) ============
    suspend fun createZarinpalInvoice(userId: Int): Result<CreatePremiumInvoiceResponse> {
        return runCatching {
            val response = apiService.createPremiumInvoice(userId)
            logD("createZarinpalInvoice: error=${response.error} authority=${response.authority}")

            if (response.error) {
                return Result.Error(
                    response.errorMsg
                            ?: "خطا در ساخت فاکتور"
                )
            }

            if (response.payUrl.isNullOrBlank()) {
                return Result.Error("آدرس پرداخت دریافت نشد")
            }

            Result.Success(response)
        }.getOrElse { e ->
            logE("createZarinpalInvoice error $e")
            Result.Error("خطا در ارتباط با سرور: ${e.message}")
        }
    }

    // ============ شعرهای دارای سبک (ویس) ============
    suspend fun getPoemsWithAudio(userId: Int): Result<DataResponse> {
        return runCatching {
            val response = apiService.getPoemsWithAudio(userId)

            if (response.error) {
                return Result.Error(
                    response.errorMsg
                            ?: "خطا در دریافت شعرها"
                )
            }

            Result.Success(response)
        }.getOrElse { e ->
            logE("getPoemsWithAudio error $e")
            Result.Error("خطا در ارتباط با سرور: ${e.message}")
        }
    }
}
