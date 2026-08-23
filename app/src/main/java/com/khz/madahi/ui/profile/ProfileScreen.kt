// ui/profile/ProfileScreen.kt
package com.khz.madahi.ui.profile

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.ui.common.BottomBarActions
import com.khz.madahi.ui.common.BottomTab
import com.khz.madahi.ui.components.BaseScreen
import com.khz.madahi.ui.components.Delete3DButton
import com.khz.madahi.ui.components.GlassCard
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.components.Gold3DButton
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.goldLight
import com.khz.madahi.ui.theme.primaryDark
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary
import com.khz.madahi.ui.theme.textSecondary

// ============================================================
// صفحه پروفایل — سبک شیشه‌ای و سه‌بعدی
// ------------------------------------------------------------
// - اطلاعات کاربر از PreferencesManager
// - آمار واقعی (دسته‌بندی / نوحه / روضه) از دیتابیس محلی
// - حالت مهمان: دکمه ورود / ثبت‌نام
// - خروج از حساب: پاک‌سازی نشست + هدایت به صفحه لگین
// ============================================================

@Composable
fun ProfileScreen(
    bottomBarActions: BottomBarActions,
    onNavigateToLogin: () -> Unit
) {
    val colors = LocalMadahiColors.current
    val context = LocalContext.current

    // ============ ViewModel ============
    val viewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(
            preferencesManager = PreferencesManager(context),
            appDatabase = AppDatabase.getInstance(context)
        )
    )

    val uiState by viewModel.uiState.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val stats by viewModel.stats.collectAsState()

    // ============ وضعیت دیالوگ تأیید خروج ============
    var showLogoutDialog by remember { mutableStateOf(false) }

    // ✅ هنگام برگشت به صفحه (مثلاً بعد از ورود/خروج) پروفایل را دوباره بخوان
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadProfile()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    BaseScreen(
        bottomBarActions = bottomBarActions,
        title = "پروفایل",
        subtitle = "",
        selectedBottomTab = BottomTab.PROFILE,
        onHeaderBottonClicked = {},
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp)
        ) {

            // ============ کارت اصلی پروفایل ============
            GlassCard3D(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // آواتار — حرف اول نام
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .shadow(18.dp, CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        colors.primary,
                                        colors.primaryDark
                                    )
                                ),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stats.fullName.trim().firstOrNull()?.toString() ?: "؟",
                            color = colors.goldLight,
                            fontSize = 44.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = stats.fullName,
                        color = colors.textPrimary,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = stats.mobile,
                        color = colors.textSecondary,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            if (isLoggedIn) {

                // ============ آمار واقعی ============
                when (uiState) {
                    is ProfileUiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    else -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            ProfileStatCard(
                                value = stats.categoryCount,
                                label = "دسته‌بندی",
                                gold = colors.gold,
                                modifier = Modifier.weight(1f)
                            )

                            ProfileStatCard(
                                value = stats.nohehCount + stats.roozehCount,
                                label = "شعرها",
                                gold = colors.gold,
                                modifier = Modifier.weight(1f)
                            )

//                            ProfileStatCard(
//                                value = stats.roozehCount,
//                                label = "روضه",
//                                gold = colors.gold,
//                                modifier = Modifier.weight(1f)
//                            )
                        }
                    }
                }

                Spacer(Modifier.height(22.dp))

                // ============ خروج از حساب (با دیالوگ تأیید) ============
                Delete3DButton(
                    text = "خروج از حساب",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { showLogoutDialog = true }
                )
            } else {

                // ============ حالت مهمان ============
                Gold3DButton(
                    text = "ورود / ثبت‌نام",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onNavigateToLogin
                )
            }

            Spacer(Modifier.weight(1f))

            Text(
                text = "ساخته‌شده با ❤️",
                color = colors.textMuted,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                textAlign = TextAlign.Center
            )
        }
    }

    // ============ دیالوگ تأیید خروج ============
    if (showLogoutDialog) {
        LogoutDialog(
            onConfirm = {
                showLogoutDialog = false
                viewModel.logout()
                onNavigateToLogin()
            },
            onDismiss = { showLogoutDialog = false }
        )
    }
}

// ============================================================
// کارت آمار
// ============================================================

@Composable
private fun ProfileStatCard(
    value: Int,
    label: String,
    gold: Color,
    modifier: Modifier = Modifier
) {
    val colors = LocalMadahiColors.current

    GlassCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$value",
                color = gold,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = label,
                color = colors.textMuted,
                fontSize = 13.sp
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun ProfileScreenPreview() {
    MadahiThemeGreen(darkTheme = true) {
        ProfileScreen(
            bottomBarActions = BottomBarActions(),
            onNavigateToLogin = {}
        )
    }
}
