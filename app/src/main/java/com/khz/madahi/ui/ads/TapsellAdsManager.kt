package com.khz.madahi.ui.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.khz.madahi.BuildConfig
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.TapsellPlusInitListener
import ir.tapsell.plus.model.AdNetworkError
import ir.tapsell.plus.model.AdNetworks

object TapsellAdsManager {

    private const val TAG = "TapsellAdsManager"

    private var initialized = false

    fun initialize(context: Context) {

        if (initialized) {
            return
        }

        TapsellPlus.initialize(
            context,
            BuildConfig.TAPSELL_KEY,
            object : TapsellPlusInitListener {

                override fun onInitializeSuccess(
                    adNetworks: AdNetworks
                ) {
                    initialized = true

                    Log.d(
                        TAG,
                        "Tapsell initialized: ${adNetworks.name}"
                    )
                }

                override fun onInitializeFailed(
                    adNetworks: AdNetworks,
                    adNetworkError: AdNetworkError
                ) {
                    Log.e(
                        TAG,
                        "Tapsell initialization failed: " + "${adNetworks.name} - " + adNetworkError.getErrorMessage()
                    )
                }
            })
    }

    fun isInitialized(): Boolean {
        return initialized
    }

    fun requestInterstitial(
        zoneId: String,
        onReady: (String) -> Unit,
        onError: (String) -> Unit = {}
    ) {
        TapsellInterstitial.request(
            zoneId = zoneId,
            onReady = onReady,
            onError = onError
        )
    }

    fun showInterstitial(
        activity: Activity,
        responseId: String,
        onOpened: () -> Unit = {},
        onClosed: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        TapsellInterstitial.show(
            activity = activity,
            responseId = responseId,
            onOpened = onOpened,
            onClosed = onClosed,
            onError = onError
        )
    }

    fun requestRewarded(
        zoneId: String,
        onReady: (String) -> Unit,
        onError: (String) -> Unit = {}
    ) {
        TapsellRewarded.request(
            zoneId = zoneId,
            onReady = onReady,
            onError = onError
        )
    }

    fun showRewarded(
        activity: Activity,
        responseId: String,
        onOpened: () -> Unit = {},
        onClosed: () -> Unit = {},
        onRewarded: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        TapsellRewarded.show(
            activity = activity,
            responseId = responseId,
            onOpened = onOpened,
            onClosed = onClosed,
            onRewarded = onRewarded,
            onError = onError
        )
    }
}