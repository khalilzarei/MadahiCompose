package com.khz.madahi.ui.audio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary

@Composable
fun UploadingDialog() {
    val colors = LocalMadahiColors.current

    Dialog(
        onDismissRequest = {
            // هیچ کاری نکن – نباید با Back یا کلیک بیرون بسته شود
        },
        properties = DialogProperties(
            dismissOnBackPress = false,   // جلوگیری از Back
            dismissOnClickOutside = false, // جلوگیری از کلیک بیرون
            usePlatformDefaultWidth = false // تمامصفحه
        )
    ) {
        BoxWithBackground()
    }
}

@Composable
private fun BoxWithBackground() {
    val colors = LocalMadahiColors.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        GlassCard3D(
            modifier = Modifier.padding(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = colors.gold
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "در حال آپلود ویس...",
                    color = colors.textPrimary,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "لطفاً صبر کنید",
                    color = colors.textMuted,
                    fontSize = 13.sp
                )
            }
        }
    }
}