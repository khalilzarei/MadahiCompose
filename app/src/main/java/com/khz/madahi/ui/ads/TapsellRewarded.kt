package com.khz.madahi.ui.ads

import android.app.Activity
import android.util.Log
import ir.tapsell.plus.TapsellPlus

object TapsellRewarded {

    private const val TAG = "TapsellRewarded"

    private var responseId: String? = null

    fun request(
        zoneId: String,
        onReady: (String) -> Unit,
        onError: (String) -> Unit = {}
    ) {

        TapsellPlus.requestRewardedVideoAd(
            zoneId
        ) { response ->

            responseId = response

            Log.d(
                TAG,
                "Rewarded ready: $response"
            )

            onReady(response)

        }
            .catch { throwable ->

                val message = throwable.message
                        ?: "Unknown error"

                responseId = null

                Log.e(
                    TAG,
                    "Rewarded request failed: $message"
                )

                onError(message)
            }
    }

    fun show(
        activity: Activity,
        responseId: String? = this.responseId,
        onOpened: () -> Unit = {},
        onClosed: () -> Unit = {},
        onRewarded: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {

        val id = responseId

        if (id.isNullOrEmpty()) {

            onError(
                "Rewarded responseId is empty"
            )

            return
        }

        TapsellPlus.showRewardedVideoAd(
            id,

            onOpened = {
                Log.d(
                    TAG,
                    "Rewarded opened"
                )

                onOpened()
            },

            onRewarded = {
                Log.d(
                    TAG,
                    "User rewarded"
                )

                onRewarded()
            },

            onError = { error ->

                val message = error.toString()

                Log.e(
                    TAG,
                    "Rewarded show failed: $message"
                )

                this.responseId = null

                onError(message)
            })

        this.responseId = null
    }

    fun clear() {
        responseId = null
    }
}