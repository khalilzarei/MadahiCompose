// ui/appselection/AppSelectionScreen.kt
package com.khz.madahi.ui.appselection

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Note
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.components.MadahiBackground
import com.khz.madahi.ui.poems.PremiumDialog
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.goldLight
import com.khz.madahi.ui.theme.primaryDark
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary

@Composable
fun AppSelectionScreen(
    onNavigateToCategory: () -> Unit,
    onNavigateToBooklet: () -> Unit
) {
    val context = LocalContext.current
    var showPremiumDialog by remember { mutableStateOf(false) }

    // ============ بررسی وضعیت پرمیوم ============
    val viewModel: AppSelectionViewModel = viewModel(
        factory = AppSelectionViewModelFactory(
            preferencesManager = PreferencesManager(context)
        )
    )
    val isPremium by viewModel.isPremium.collectAsState()

    AppSelectionContent(
        isPremium = isPremium,
        onNavigateToCategory = onNavigateToCategory,
        onNavigateToBooklet = {
            if (isPremium) {
                onNavigateToBooklet()
            } else {
                showPremiumDialog = true
            }
        })

    // ============ دیالوگ پرمیوم ============
    if (showPremiumDialog) {
        PremiumDialog(
            onDismiss = { showPremiumDialog = false },
        )

    }
}

@Composable
private fun AppSelectionContent(
    isPremium: Boolean,
    onNavigateToCategory: () -> Unit,
    onNavigateToBooklet: () -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MadahiBackground {
            val colors = LocalMadahiColors.current

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 24.dp,
                        vertical = 24.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {


                Text(
                    text = "مداحی",
                    color = colors.textPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "بخش مورد نظر خود را انتخاب کنید",
                    color = colors.textMuted,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(36.dp))

                SelectionCard(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Note,
                            contentDescription = null,
                            tint = colors.gold,
                            modifier = Modifier.size(40.dp)
                        )
                    },
                    title = "دفترچه",
                    subtitle = "دسته‌بندی، شعرها و مدیریت محتوا",
                    badge = null,
                    onClick = onNavigateToCategory
                )

                Spacer(Modifier.height(18.dp))

                SelectionCard(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = if (isPremium) colors.gold else colors.textMuted,
                            modifier = Modifier.size(40.dp)
                        )
                    },
                    title = "کتابچه",
                    subtitle = if (isPremium) "کتابچه مداحی" else "ویژه نسخه پرمیوم",
                    badge = if (!isPremium) "پرمیوم" else null,
                    onClick = onNavigateToBooklet
                )
            }
        }
    }
}

// ============================================================
// کارت انتخاب — شیشه‌ای سه‌بعدی
// ============================================================

@Composable
private fun SelectionCard(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    badge: String?,
    onClick: () -> Unit
) {
    val colors = LocalMadahiColors.current

    GlassCard3D(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 22.dp,
                    vertical = 24.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // آیکون
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        brush = Brush.radialGradient(
                            listOf(
                                colors.goldLight.copy(alpha = 0.4f),
                                colors.gold.copy(alpha = 0.15f)
                            )
                        ),
                        shape = RoundedCornerShape(18.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(18.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }

            Spacer(Modifier.width(18.dp))

            // متن
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        color = colors.textPrimary,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // نشان پرمیوم
                    badge?.let { label ->
                        Spacer(Modifier.width(10.dp))
                        Box(
                            modifier = Modifier
                                .background(
                                    brush = Brush.horizontalGradient(
                                        listOf(
                                            colors.gold.copy(alpha = 0.85f),
                                            colors.goldLight.copy(alpha = 0.85f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(
                                    horizontal = 10.dp,
                                    vertical = 4.dp
                                )
                        ) {
                            Text(
                                text = label,
                                color = colors.primaryDark,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(Modifier.height(6.dp))

                Text(
                    text = subtitle,
                    color = colors.textMuted,
                    fontSize = 13.sp
                )
            }

        }
    }
}

// ============================================================
// Preview
// ============================================================

@Preview(showBackground = false)
@Composable
private fun AppSelectionScreenPreview() {
    MadahiThemeGreen(darkTheme = true) {
        AppSelectionContent(
            isPremium = true,
            onNavigateToCategory = {},
            onNavigateToBooklet = {})
    }
}