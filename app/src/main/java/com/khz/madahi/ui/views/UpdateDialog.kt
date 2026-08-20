// ui/views/UpdateDialog.kt
package com.khz.madahi.ui.views

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.khz.madahi.ui.common.DialogFullscreenWindow
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.components.ThreeDButton
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiTheme
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary

// ============================================================
// دیالوگ آپدیت اپ — طراحی شیشه‌ای و سه‌بعدی
// امضا و رفتار دقیقاً مثل نسخه قبلی است
// ============================================================

@Composable
fun UpdateDialog(
    showDialog: Boolean,
    updateUrl: String,
    onDismiss: () -> Unit,
    onUpdate: () -> Unit,
    onLater: () -> Unit
) {
    if (!showDialog) return


    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            UpdateDialogContent(
                updateUrl = updateUrl,
                onDismiss = onDismiss,
                onUpdate = onUpdate,
                onLater = onLater,
            )
        }
    }
}

@Composable
fun UpdateDialogContent(
    updateUrl: String,
    onDismiss: () -> Unit,
    onUpdate: () -> Unit,
    onLater: () -> Unit
) {
    DialogFullscreenWindow()

    val context = LocalContext.current
    val colors = LocalMadahiColors.current

    GlassCard3D(
        modifier = Modifier
            .fillMaxWidth(0.86f)
            .padding(vertical = 24.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 28.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ============ آیکون آپدیت ============
            Text(
                text = "🔄",
                fontSize = 48.sp
            )

            Spacer(Modifier.height(14.dp))

            // ============ عنوان ============
            Text(
                text = "آپدیت",
                color = colors.textPrimary,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))

            // ============ متن ============
            Text(
                text = "نسخه جدید اپلیکیشن آماده است!\nلطفاً اپ را آپدیت کنید.",
                color = colors.textMuted,
                fontSize = 15.sp,
                lineHeight = 26.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(26.dp))

            // ============ دکمه‌ها ============
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
            ) {

                ThreeDButton(
                    text = "بروزرسانی",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onUpdate()
                        val browserIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(updateUrl)
                        )
                        context.startActivity(browserIntent)
                        (context as? ComponentActivity)?.finish()
                    })

                ThreeDButton(
                    text = "بعداً",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onLater
                )
            }
        }
    }
}

@Preview(
    name = "Delete Dialog - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun UpdateDialogPreviewDark() {
    MadahiTheme(darkTheme = true) {

        UpdateDialogContent(
            updateUrl = "",
            onDismiss = {},
            onUpdate = {},
            onLater = {},
        )

    }
}