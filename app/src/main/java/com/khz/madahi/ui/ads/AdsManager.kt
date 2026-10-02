package com.khz.madahi.ui.ads

import android.app.Activity
import android.content.Context
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.TapsellPlus.requestInterstitialAd

object AdsManager {

    fun initialize(context: Context) {
        TapsellPlus.initialize()
    }

    fun showInterstitial(
        activity: Activity,
        onClosed: () -> Unit = {}
    ) {
        requestInterstitialAd(
            activity = activity,
            zoneId = AdsConfig.INTERSTITIAL_ZONE_ID,
            listener = object : TapsellPlus.AdRequestListener {

                override fun onResponse(
                    responseId: String
                ) {
                    TapsellPlus.showInterstitialAd(
                        activity = activity,
                        responseId = responseId,
                        listener = object : TapsellPlus.AdShowListener {

                            override fun onOpened() {
                            }

                            override fun onClosed() {
                                onClosed()
                            }

                            override fun onError(
                                message: String?
                            ) {
                                onClosed()
                            }
                        })
                }

                override fun onError(
                    message: String?
                ) {
                    onClosed()
                }
            })
    }
}