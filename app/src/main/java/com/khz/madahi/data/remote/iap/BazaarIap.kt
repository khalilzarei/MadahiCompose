// data/remote/iap/BazaarIap.kt
package com.khz.madahi.data.remote.iap

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.android.vending.billing.IInAppBillingService
import com.khz.madahi.BuildConfig

// ============================================================
// اتصال به سرویس پرداخت درون‌برنامه‌ای بازار
// ------------------------------------------------------------
// - اتصال (bind) به سرویس IInAppBillingService
// - گرفتن BUY_INTENT برای شروع خرید
// - خواناتر بودن نسبت به کار مستقیم با ServiceConnection
// ============================================================

object BazaarIap {

    private const val TAG = "BazaarIap"
    private const val BIND_ACTION = "com.android.vending.billing.InAppBillingService.BIND"
    private const val SERVICE_PACKAGE = "ir.mobapp.bazaar"
    const val API_VERSION = 3
    const val SKU_TYPE_INAPP = "inapp"

    private var billingService: IInAppBillingService? = null
    private var connectInProgress = false
    private val waitingCallbacks = mutableListOf<(IInAppBillingService?) -> Unit>()

    // ================== نصب بودن بازار ==================
    fun isBazaarInstalled(context: Context): Boolean {
        return try {
            context.packageManager.getPackageInfo(
                SERVICE_PACKAGE,
                0
            )
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    // ================== اتصال به سرویس ==================
    fun connect(
        context: Context,
        onResult: (IInAppBillingService?) -> Unit
    ) {
        val existing = billingService
        if (existing != null) {
            onResult(existing)
            return
        }

        // اگر اتصال در حال انجام است، کال‌بک را صف بکن
        if (connectInProgress) {
            waitingCallbacks.add(onResult)
            return
        }

        connectInProgress = true
        val connection = object : android.content.ServiceConnection {
            override fun onServiceConnected(
                name: ComponentName?,
                service: IBinder?
            ) {
                val bound = IInAppBillingService.Stub.asInterface(service)
                billingService = bound
                connectInProgress = false
                Log.d(
                    TAG,
                    "onServiceConnected ✅"
                )

                onResult(bound)
                waitingCallbacks.toList()
                    .forEach { it(bound) }
                waitingCallbacks.clear()
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                Log.w(
                    TAG,
                    "onServiceDisconnected"
                )
                billingService = null
            }
        }

        val intent = Intent(BIND_ACTION).apply { setPackage(SERVICE_PACKAGE) }
        val bound = try {
            context.applicationContext.bindService(
                intent,
                connection,
                Context.BIND_AUTO_CREATE
            )
        } catch (e: Exception) {
            Log.e(
                TAG,
                "bindService error: $e"
            )
            false
        }

        if (!bound) {
            connectInProgress = false
            Log.e(
                TAG,
                "بازار نصب نیست یا سرویس در دسترس نیست"
            )
            onResult(null)
        }
    }

    // ================== شروع خرید ==================
    /**
     * اگر BUY_INTENT معتبر برگشت، PendingIntent برای استارت با ActivityResult را برمی‌گرداند.
     * در غیر این صورت null.
     */
    fun getBuyIntentPendingIntent(sku: String): PendingIntent? {
        val service = billingService
                ?: run {
                    Log.e(
                        TAG,
                        "getBuyIntent: سرویس متصل نیست"
                    )
                    return null
                }

        return try {
            val bundle: Bundle = service.getBuyIntent(
                API_VERSION,
                BuildConfig.APPLICATION_ID,
                sku,
                SKU_TYPE_INAPP,
                "madahi_pro"
            )

            val responseCode = bundle.getInt(
                "RESPONSE_CODE",
                -1
            )
            Log.d(
                TAG,
                "getBuyIntent responseCode=$responseCode"
            )

            if (responseCode == 0) {
                bundle.getParcelable<PendingIntent>("BUY_INTENT")
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(
                TAG,
                "getBuyIntent error: $e"
            )
            null
        }
    }
}
