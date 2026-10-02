package com.khz.madahi.ui.ads

import android.util.Log
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.TapsellPlusBannerType
import ir.tapsell.plus.TapsellPlusHorizontalGravity
import ir.tapsell.plus.TapsellPlusVerticalGravity

object TapsellBanner {

    private const val TAG = "TapsellBanner"

    private var responseId: String? = null

    fun request(
        zoneId: String,
        onReady: (String) -> Unit,
        onError: (String) -> Unit = {}
    ) {

        TapsellPlus.requestStandardBannerAd(
            zoneId,
            TapsellPlusBannerType.BANNER_320x50,

            onResponse = { response ->

                val id = response["response_id"]?.toString()
                    .orEmpty()

                if (id.isEmpty()) {

                    onError(
                        "Banner responseId is empty"
                    )

                    return@requestStandardBannerAd
                }

                responseId = id

                Log.d(
                    TAG,
                    "Banner ready: $id"
                )

                onReady(id)
            },

            onError = { error ->

                val message = error.toString()

                Log.e(
                    TAG,
                    "Banner request failed: $message"
                )

                onError(message)
            })
    }

    fun show(
        responseId: String? = this.responseId
    ) {

        val id = responseId

        if (id.isNullOrEmpty()) {
            return
        }

        TapsellPlus.showStandardBannerAd(
            id,
            TapsellPlusHorizontalGravity.CENTER,
            TapsellPlusVerticalGravity.BOTTOM,

            onOpened = {
                Log.d(
                    TAG,
                    "Banner opened"
                )
            },

            onError = { error ->

                Log.e(
                    TAG,
                    "Banner show failed: $error"
                )
            })
    }

    fun hide() {
        TapsellPlus.hideStandardBanner()
    }

    fun destroy() {

        responseId?.let {
            TapsellPlus.destroyStandardBanner(it)
        }

        responseId = null
    }
}