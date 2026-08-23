// ui/setting/SettingScreen.kt
package com.khz.madahi.ui.setting

import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.ui.common.BottomBarActions
import com.khz.madahi.ui.common.BottomTab
import com.khz.madahi.ui.components.BaseScreen
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.components.Mini3DButton
import com.khz.madahi.ui.theme.FontCatalog
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.textPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    bottomBarActions: BottomBarActions
) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }

    var isDarkMode by remember {
        mutableStateOf(preferencesManager.isNightMode)
    }
    val colors = LocalMadahiColors.current
    BaseScreen(
        bottomBarActions = bottomBarActions,
        title = "تنظیمات",
        subtitle = "",
        isCategory = true,
        selectedBottomTab = BottomTab.SETTINGS,
        onHeaderBottonClicked = {},
    ) {

        //region BODY
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
//            contentAlignment = Alignment.Center
        ) {

            GlassCard3D {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // ✅ دارک مد
                    GlassCard3D {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isDarkMode) {
                                        Icons.Default.DarkMode
                                    } else {
                                        Icons.Default.LightMode
                                    },
                                    contentDescription = null,
                                    tint = if (isDarkMode) Color.Black else colors.gold,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "حالت شب",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = if (isDarkMode) "فعال 🌙" else "غیرفعال ☀️",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Switch(
                                checked = isDarkMode,
                                onCheckedChange = { isChecked ->
                                    isDarkMode = isChecked
                                    preferencesManager.isNightMode = isChecked
                                    (context as? ComponentActivity)?.recreate()
                                },
                                colors = SwitchDefaults.colors(
                                    checkedTrackColor = colors.gold,
                                    checkedThumbColor = colors.textPrimary
                                )
                            )

                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(20.dp),
                        thickness = DividerDefaults.Thickness,
                        color = MaterialTheme.colorScheme.outline
                    )

                    //region ✅ تنظیمات فونت (نمونه)
// ============ انتخاب فونت ============
                    var showFontDialog by remember { mutableStateOf(false) }
                    val currentFontKey = preferencesManager.font

                    GlassCard3D(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showFontDialog = true }) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "✒️",
                                    fontSize = 22.sp
                                )
//                                Icon(
//                                    imageVector = Icons.Default.TextFields,
//                                    contentDescription = null,
//                                    tint = MaterialTheme.colorScheme.primary,
//                                    modifier = Modifier.size(24.dp)
//                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "فونت",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            // نام فونت انتخابی — با فونت خودش نمایش داده می‌شود
                            Text(
                                text = FontCatalog.availableFonts[currentFontKey]
                                        ?: "وزیر",
                                fontFamily = FontCatalog.fontFamilyFor(currentFontKey),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

// ============ دیالوگ انتخاب فونت (طراحی شیشه‌ای سه‌بعدی) ============
                    if (showFontDialog) {
                        FontDialog(
                            currentFontKey = currentFontKey,
                            onFontSelected = { key ->
                                preferencesManager.font = key
                                showFontDialog = false
                                // 🔄 بازسازی اکتیویتی تا فونت کل اپ عوض شود
                                (context as? ComponentActivity)?.recreate()
                            },
                            onDismiss = { showFontDialog = false }
                        )
                    }

                    //endregion

                    HorizontalDivider(
                        modifier = Modifier.padding(20.dp),
                        thickness = DividerDefaults.Thickness,
                        color = MaterialTheme.colorScheme.outline
                    )

                    //region  ✅ اندازه فونت

                    GlassCard3D {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
//                                Icon(
//                                    imageVector = Icons.Default.FormatSize,
//                                    contentDescription = null,
//                                    tint = MaterialTheme.colorScheme.primary,
//                                    modifier = Modifier.size(24.dp)
//                                )

                                Text(
                                    text = "🔠",
                                    fontSize = 22.sp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "اندازه فونت",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Mini3DButton(
                                    imageVector = Icons.Default.Remove,
                                    tint = colors.gold,
                                    onClick = {
                                        if (preferencesManager.fontSize > 11) {
                                            preferencesManager.fontSize--
                                            (context as? ComponentActivity)?.recreate()
                                        }
                                    },
                                )
                                Text(
                                    text = "${preferencesManager.fontSize}",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )

                                Mini3DButton(
                                    imageVector = Icons.Default.Add,
                                    tint = colors.gold,
                                    onClick = {
                                        if (preferencesManager.fontSize < 35) {
                                            preferencesManager.fontSize++
                                            (context as? ComponentActivity)?.recreate()
                                        }
                                    },
                                )

                            }
                        }
                    }

                    //endregion

                    HorizontalDivider(
                        modifier = Modifier.padding(20.dp),
                        thickness = DividerDefaults.Thickness,
                        color = MaterialTheme.colorScheme.outline
                    )

                    GlassCard3D {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { bottomBarActions.onAboutClick() }
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = colors.gold,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "درباره ما",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = null,
                                tint = colors.gold,
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(20.dp),
                        thickness = DividerDefaults.Thickness,
                        color = MaterialTheme.colorScheme.outline
                    )

                    GlassCard3D {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { bottomBarActions.onProfileClick() }
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = colors.gold,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "پروفایل",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = null,
                                tint = colors.gold,
                            )
                        }
                    }
                }
            }

        }

        //endregion
    }
}

@Preview(showSystemUi = true)
@Composable
fun SettingScreenPreview() {
    MadahiThemeGreen(darkTheme = true) {
        SettingScreen(bottomBarActions = BottomBarActions())
    }
}