package com.khz.madahi.ui.ads.components

import android.app.Activity
import android.util.Log
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.khz.madahi.ui.ads.TapsellNative

@Composable
fun TapsellNativeAd(
    zoneId: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity
            ?: return

    val nativeAd = remember(activity) {
        TapsellNative(activity)
    }

    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            FrameLayout(context).apply {
                nativeAd.createAdHolder(this)

                nativeAd.request(
                    zoneId = zoneId,
                    onReady = {
                        nativeAd.show()
                    },
                    onError = { error ->
                        Log.e(
                            "TapsellNative",
                            "Request error: $error"
                        )
                    })
            }
        },
        update = {
            // Nothing
        })

    DisposableEffect(nativeAd) {
        onDispose {
            nativeAd.destroy()
        }
    }
}