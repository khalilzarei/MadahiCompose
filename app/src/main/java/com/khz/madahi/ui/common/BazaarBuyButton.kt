// ui/common/BazaarBuyButton.kt
package com.khz.madahi.ui.common

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.khz.madahi.data.remote.iap.BazaarIap
import com.khz.madahi.ui.components.Gold3DButton

// ============================================================
// دکمه خرید از بازار — کل فلو خرید در یک کامپوزابل
// ------------------------------------------------------------
// کلیک → اتصال به سرویس بازار → BUY_INTENT → نتیجه:
//   onSuccess(purchaseData) = خرید موفق
//   onFail(msg)             = خطا واقعی (انصراف کاربر شامل نمی‌شود)
// ============================================================

@Composable
fun BazaarBuyButton(
    text: String = "خرید از بازار",
    modifier: Modifier = Modifier,
    onSuccess: (purchaseData: String) -> Unit,
    onFail: (String) -> Unit = {}
) {
    val context = LocalContext.current
    var pendingBuy by remember { mutableStateOf(false) }
    var isConnecting by remember { mutableStateOf(false) }
    var isDisposed by remember { mutableStateOf(false) }
    val bazaarInstalled = remember { BazaarIap.isBazaarInstalled(context) }

    // ✅ اگر دیالوگ/صفحه حین اتصال بسته شد، launch بعدی انجام نشود
    DisposableEffect(Unit) {
        onDispose { isDisposed = true }
    }

    // ------------------------------------------------------------------
    // ⚠️ ترتیب مهم است: توابع محلی در Kotlin باید «قبل» از محل استفاده
    //    اعلام شوند.
    // ------------------------------------------------------------------

    // (۱) پردازش نتیجه خرید — قبل از buyLauncher اعلام می‌شود
    fun handlePurchaseResult(result: ActivityResult) {
        val data = result.data

        // کاربر صفحه خرید را بسته/لغو کرده — نه خطا، نه موفقیت؛ کار نمی‌کنیم
        if (result.resultCode != Activity.RESULT_OK || data == null) {
            return
        }

        val responseCode = data.getIntExtra(
            "RESPONSE_CODE",
            -1
        )

        when (responseCode) {
            0 -> {
                // خرید موفق
                val purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA")
                if (!purchaseData.isNullOrBlank()) {
                    onSuccess(purchaseData)
                } else {
                    onFail("اطلاعات خرید کامل نیست")
                }
            }

            1 -> Unit  // انصراف کاربر — خطا محسوب نمی‌شود

            else -> onFail("خرید با خطا مواجه شد (کد: $responseCode)")
        }
    }

    // (۲) لانشر — از تابع (۱) استفاده می‌کند
    val buyLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        handlePurchaseResult(result)
    }

    // (۳) شروع خرید — از buyLauncher (۲) استفاده می‌کند
    fun startPurchase() {
        isConnecting = true
        BazaarIap.connect(context) { _ ->
            isConnecting = false

            // اگر صفحه حین اتصال بسته شده، خرید را شروع نمی‌کنیم
//            val intentSender: IntentSender? = if (isDisposed) {
//                null
//            } else {
//                BazaarIap.getBuyIntentSender(BAZAAR_PRO_PRODUCT_ID)
//            }
//
//            if (intentSender != null) {
//                buyLauncher.launch(intentSender)
//            } else if (!isDisposed) {
//                onFail("امکان اتصال به بازار وجود ندارد")
//            }
        }
    }

    // (۴) کلیک روی دکمه
    LaunchedEffect(pendingBuy) {
        if (pendingBuy) {
            pendingBuy = false
            startPurchase()
        }
    }

    // ============ نمایش ============
    if (isConnecting) {
        // حین اتصال به بازار
        Gold3DButton(
            text = "در حال اتصال به بازار…",
            modifier = modifier,
            enabled = false,
            onClick = {})
    } else {
        Gold3DButton(
            text = if (bazaarInstalled) text else "بازار را نصب کنید",
            modifier = modifier,
            enabled = bazaarInstalled,
            onClick = { pendingBuy = true })
    }
}
