// ui/views/CustomDialog.kt
package com.khz.madahi.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary

// ============================================================
// دیالوگ عمومی (پیام/خطا) — طراحی شیشه‌ای و سه‌بعدی
// امضا و رفتار دقیقاً مثل نسخه قبلی است
// ============================================================

@Composable
fun CustomDialog(
    showDialog: Boolean,
    title: String = "آپدیت",
    message: String = "نسخه جدید اپلیکیشن آماده است! لطفاً اپ را آپدیت کنید.",
    confirmText: String = "بروزرسانی",
    dismissText: String = "بعداً",
    icon: ImageVector = androidx.compose.material.icons.Icons.Default.SystemUpdate,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onDismissAction: () -> Unit = {}
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
            DialogFullscreenWindow()

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

                    // ============ آیکون ============
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = colors.gold,
                        modifier = Modifier.size(52.dp)
                    )

                    Spacer(Modifier.height(14.dp))

                    // ============ عنوان ============
                    Text(
                        text = title,
                        color = colors.textPrimary,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(12.dp))

                    // ============ پیام ============
                    Text(
                        text = message,
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
                            text = confirmText,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = onConfirm
                        )

                        // اگر dismissText خالی بود، دکمه دوم نمایش داده نمی‌شود
                        if (dismissText.isNotEmpty()) {
                            ThreeDButton(
                                text = dismissText,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = onDismissAction
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    name = "Dialog Preview",
    showBackground = false,
    widthDp = 400,
    heightDp = 350
)
@Composable
fun CustomDialogPreview() {
    MadahiTheme(darkTheme = true) {
        // ✅ مستقیماً CustomDialog را صدا بزن
        CustomDialog(
            showDialog = true,
            title = "آپدیت",
            message = "نسخه جدید اپلیکیشن آماده است! اپ را آپدیت کنید.",
            confirmText = "بروزرسانی",
            dismissText = "بعداً",
            icon = Icons.Default.SystemUpdate,
            onDismiss = {},
            onConfirm = {},
            onDismissAction = {})
    }
}