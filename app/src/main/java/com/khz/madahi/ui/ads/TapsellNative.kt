package com.khz.madahi.ui.ads

import android.app.Activity
import android.util.Log
import android.widget.FrameLayout
import ir.tapsell.plus.AdHolder
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel

class TapsellNative(
    private val activity: Activity
) {

    companion object {
        private const val TAG = "TapsellNative"
    }

    private var responseId: String? = null
    private var adHolder: AdHolder? = null

    /**
     * ساخت Native Ad Holder
     *
     * container همان FrameLayout است که تبلیغ داخل آن نمایش داده می‌شود.
     */
    fun createAdHolder(
        container: FrameLayout
    ) {
        adHolder = TapsellPlus.createAdHolder(
            activity,
            container,
            ir.tapsell.plus.R.layout.native_banner
        )
    }

    /**
     * درخواست تبلیغ Native
     */
    fun request(
        zoneId: String,
        onReady: ((responseId: String) -> Unit)? = null,
        onError: ((message: String) -> Unit)? = null
    ) {

        if (zoneId.isBlank()) {
            onError?.invoke("Tapsell Native zoneId is empty")
            return
        }

        TapsellPlus.requestNativeAd(
            activity,
            zoneId,
            object : AdRequestCallback() {

                override fun response(
                    tapsellPlusAdModel: TapsellPlusAdModel
                ) {
                    super.response(tapsellPlusAdModel)

                    if (activity.isFinishing || activity.isDestroyed) {
                        return
                    }

                    val id = tapsellPlusAdModel.responseId

                    if (id.isNullOrBlank()) {
                        Log.e(
                            TAG,
                            "Native responseId is empty"
                        )
                        onError?.invoke("Native responseId is empty")
                        return
                    }

                    responseId = id

                    Log.d(
                        TAG,
                        "Native ad ready: $id"
                    )

                    onReady?.invoke(id)
                }

                override fun error(message: String) {
                    super.error(message)

                    Log.e(
                        TAG,
                        "Native request error: $message"
                    )

                    onError?.invoke(message)
                }
            })
    }

    /**
     * نمایش Native Ad
     */
    fun show(
        onOpened: (() -> Unit)? = null,
        onError: ((message: String) -> Unit)? = null
    ) {

        val id = responseId

        if (id.isNullOrBlank()) {
            onError?.invoke(
                "Native ad is not ready. Call request() first."
            )
            return
        }

        val holder = adHolder

        if (holder == null) {
            onError?.invoke(
                "Native AdHolder is not created. Call createAdHolder() first."
            )
            return
        }

        TapsellPlus.showNativeAd(
            activity,
            id,
            holder,
            object : AdShowListener() {

                override fun onOpened(
                    tapsellPlusAdModel: TapsellPlusAdModel
                ) {
                    super.onOpened(tapsellPlusAdModel)

                    Log.d(
                        TAG,
                        "Native ad opened"
                    )

                    onOpened?.invoke()
                }

                override fun onError(
                    tapsellPlusErrorModel: TapsellPlusErrorModel
                ) {
                    super.onError(tapsellPlusErrorModel)

                    val message = tapsellPlusErrorModel.errorMessage

                    Log.e(
                        TAG,
                        "Native show error: $message"
                    )

                    onError?.invoke(message)
                }
            })
    }

    /**
     * حذف تبلیغ Native
     */
    fun destroy() {

        val id = responseId

        if (!id.isNullOrBlank()) {
            TapsellPlus.destroyNativeBanner(
                activity,
                id
            )
        }

        responseId = null
        adHolder = null

        Log.d(
            TAG,
            "Native ad destroyed"
        )
    }
}