package com.khz.madahi.ui.ads

import android.app.Activity

object TapsellInterstitial {

    private const val TAG = "TapsellInterstitial"

    private var responseId: String? = null

    fun request(
        zoneId: String,
        onReady: (String) -> Unit,
        onError: (String) -> Unit = {}
    ) {

//        requestInterstitialAd(
//            zoneId
//        ) { response ->
//
//            responseId = response
//
//            Log.d(
//                TAG,
//                "Interstitial ready: $response"
//            )
//
//            onReady(response)
//
//        }
//            .catch { throwable ->
//
//                val message = throwable.message
//                        ?: "Unknown error"
//
//                responseId = null
//
//                Log.e(
//                    TAG,
//                    "Interstitial request failed: $message"
//                )
//
//                onError(message)
//            }
    }

    fun show(
        activity: Activity,
        responseId: String? = this.responseId,
        onOpened: () -> Unit = {},
        onClosed: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {

        val id = responseId

        if (id.isNullOrEmpty()) {

            onError(
                "Interstitial responseId is empty"
            )

            return
        }

//        TapsellPlus.showInterstitialAd(
//            id,
//
//            onOpened = {
//                Log.d(
//                    TAG,
//                    "Interstitial opened"
//                )
//
//                onOpened()
//            },
//
//            onError = { error ->
//
//                val message = error.toString()
//
//                Log.e(
//                    TAG,
//                    "Interstitial show failed: $message"
//                )
//
//                this.responseId = null
//
//                onError(message)
//            })

        this.responseId = null
    }

    fun clear() {
        responseId = null
    }
}