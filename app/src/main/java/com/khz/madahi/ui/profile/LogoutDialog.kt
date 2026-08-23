// ui/profile/LogoutDialog.kt
package com.khz.madahi.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.khz.madahi.ui.components.Delete3DButton
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.components.ThreeDButton
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.delete
import com.khz.madahi.ui.theme.textMuted

// ============================================================
// دیالوگ تأیید خروج از حساب — طراحی شیشه‌ای و سه‌بعدی
// امضا دقیقاً مثل دیالوگ‌های حذف (DeleteContentDialog)
// ============================================================

@Composable
fun LogoutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            DialogFullscreenWindow()

            LogoutDialogContent(
                onConfirm = onConfirm,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
fun LogoutDialogContent(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
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

            // ============ آیکون هشدار ============
            Text(
                text = "🚪",
                fontSize = 48.sp
            )

            Spacer(Modifier.height(14.dp))

            // ============ عنوان ============
            Text(
                text = "خروج از حساب",
                color = colors.delete,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))

            // ============ متن تأیید ============
            Text(
                text = "آیا از خروج از حساب خود اطمینان دارید؟\nمی‌توانید هر زمان دوباره وارد شوید.",
                color = colors.textMuted,
                fontSize = 15.sp,
                lineHeight = 26.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(26.dp))

            // ============ دکمه‌ها ============
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                ThreeDButton(
                    text = "انصراف",
                    modifier = Modifier.weight(1f),
                    onClick = onDismiss
                )

                Delete3DButton(
                    text = "خروج",
                    modifier = Modifier.weight(1f),
                    onClick = onConfirm
                )
            }
        }
    }
}

//// ============ Preview ها ============
@Preview(showBackground = true)
@Composable
fun LogoutDialogContentPreviewDark() {
    MadahiThemeGreen(darkTheme = true) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LogoutDialogContent(
                onConfirm = {},
                onDismiss = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LogoutDialogContentPreviewLight() {
    MadahiThemeGreen(darkTheme = false) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LogoutDialogContent(
                onConfirm = {},
                onDismiss = {})
        }
    }
}
