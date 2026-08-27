// ui/poems/PremiumDialog.kt
package com.khz.madahi.ui.poems

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.ui.common.BazaarBuyButton
import com.khz.madahi.ui.common.DialogFullscreenWindow
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.components.ThreeDButton
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary
import kotlinx.coroutines.delay

// ============================================================
// دیالوگ فعال‌سازی نسخه پرو
// ------------------------------------------------------------
// خودکفا: PremiumViewModel خودش را می‌سازد، خرید بازار را
// مدیریت می‌کند و بعد از فعال‌سازی موفق onActivated را صدا می‌زند
// ============================================================

@Composable
fun PremiumDialog(
    onDismiss: () -> Unit,
    onActivated: () -> Unit
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

            val context = LocalContext.current
            val colors = LocalMadahiColors.current
            val viewModel: PremiumViewModel = viewModel(
                factory = PremiumViewModelFactory(
                    preferencesManager = PreferencesManager(context)
                )
            )
            val state by viewModel.premiumState.collectAsState()
            val invoiceState by viewModel.invoiceState.collectAsState()

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
                    Text(
                        text = "🎧",
                        fontSize = 48.sp
                    )

                    Spacer(Modifier.height(14.dp))

                    // ============ عنوان ============
                    Text(
                        text = "نسخه پرو",
                        color = colors.textPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "با یک خرید، برای همیشه فعال می‌شود",
                        color = colors.textMuted,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(20.dp))

                    // ============ امکانات پرو ============
                    PremiumFeatures()

                    Spacer(Modifier.height(24.dp))

                    // ============ پیام خطا ============
                    if (state is PremiumUiState.Error) {
                        Text(
                            text = "⚠️ ${(state as PremiumUiState.Error).message}",
                            color = colors.gold,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(14.dp))
                    }

                    // ============ دکمه خرید / وضعیت پرداخت ============
                    when {

                        state is PremiumUiState.Activating      -> {
                            ThreeDButton(
                                text = "در حال فعال‌سازی…",
                                modifier = Modifier.fillMaxWidth(),
                                enabled = false,
                                onClick = {})
                        }

                        invoiceState is InvoiceUiState.Waiting  -> {
                            // ⏳ منتظر نتیجه پرداخت زرین‌پال
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = colors.gold,
                                    strokeWidth = 2.dp
                                )
                                Spacer(Modifier.width(10.dp))
                                Text(
                                    text = "پرداخت را در صفحه‌ای که باز شد انجام دهید",
                                    color = colors.textPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "بعد از پرداخت، وضعیت به‌صورت خودکار بررسی می‌شود",
                                color = colors.textMuted,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(14.dp))
                            ThreeDButton(
                                text = "بررسی وضعیت",
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { viewModel.checkPremium() })

                            // ✅ چک خودکار هر ۵ ثانیه (تا ۱۲ بار)
                            LaunchedEffect(Unit) {
                                repeat(12) {
                                    delay(5_000)
                                    if (viewModel.premiumState.value is PremiumUiState.Pro) {
                                        return@LaunchedEffect
                                    }
                                    viewModel.checkPremium()
                                }
                            }
                        }

                        invoiceState is InvoiceUiState.Creating -> {
                            ThreeDButton(
                                text = "در حال ساخت فاکتور…",
                                modifier = Modifier.fillMaxWidth(),
                                enabled = false,
                                onClick = {})
                        }

                        else                                    -> {
                            // نمایش خطا (مثلاً: «شما قبلاً نسخه پرو را فعال کرده‌اید»)
                            if (invoiceState is InvoiceUiState.Error) {
                                Text(
                                    "⚠️ ${(invoiceState as InvoiceUiState.Error).message}",
                                    color = colors.gold,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(Modifier.height(12.dp))
                            }
                            // دو کانال خرید
                            BazaarBuyButton(
                                text = "خرید از بازار",
                                modifier = Modifier.fillMaxWidth(),
                                onSuccess = { viewModel.activateFromBazaar(it) },
                                onFail = {})
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text = "یا",
                                color = colors.textMuted,
                                fontSize = 12.sp
                            )
                            Spacer(Modifier.height(10.dp))
                            ThreeDButton(
                                text = "پرداخت از طریق زرین‌پال",
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    viewModel.startZarinpalPayment { payUrl, _ ->
                                        if (payUrl != null) {
                                            try {
                                                context.startActivity(
                                                    Intent(
                                                        Intent.ACTION_VIEW,
                                                        Uri.parse(payUrl)
                                                    )
                                                )
                                            } catch (e: Exception) {
                                                // مرورگر در دسترس نیست
                                            }
                                        }
                                    }
                                })
                        }
                    }

                    // ============ پیام بعد از فعال‌سازی ============
                    if (state is PremiumUiState.Pro) {
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "✅ نسخه پرو فعال شد",
                            color = colors.gold,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        // دیالوگ را بعد از فعال‌سازی ببند
                        LaunchedEffect(Unit) {
                            onActivated()
                        }
                    }

                    Spacer(Modifier.height(22.dp))

                    ThreeDButton(
                        text = "بستن",
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onDismiss
                    )
                }
            }
        }
    }
}

// ============================================================
// لیست امکانات پرو — قابل استفاده در جاهای دیگر
// ============================================================

@Composable
fun PremiumFeatures() {
    val colors = LocalMadahiColors.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        PremiumFeatureRow(
            icon = "📜",
            title = "نمایش شعرها با سبک‌ها",
            subtitle = "لیست شعرهای ذخیره‌شده همراه با سبک (ویس) و پخش صوت"
        )
        PremiumFeatureRow(
            icon = "🎤",
            title = "افزودن ویس به شعر",
            subtitle = "سبک مداحی دلخواه را به هر شعر اضافه کنید"
        )
    }
}

@Composable
private fun PremiumFeatureRow(
    icon: String,
    title: String,
    subtitle: String
) {
    val colors = LocalMadahiColors.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            fontSize = 22.sp
        )

        Spacer(Modifier.width(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 4.dp,
                    bottom = 4.dp,
                    start = 4.dp,
                    end = 12.dp
                )
        ) {
            Text(
                text = title,
                color = colors.textPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = colors.textMuted,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PremiumFeaturesPreview() {
    MadahiThemeGreen(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            PremiumFeatures()
        }
    }
}
