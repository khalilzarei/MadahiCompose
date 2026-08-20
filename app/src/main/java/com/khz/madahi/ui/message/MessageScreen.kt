// ui/message/MessageScreen.kt
package com.khz.madahi.ui.message

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.ui.common.BottomBarActions
import com.khz.madahi.ui.common.BottomTab
import com.khz.madahi.ui.components.BaseScreen
import com.khz.madahi.ui.components.GlassCard
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.components.Gold3DButton
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary

// ============================================================
// صفحه پیام‌ها — سبک شیشه‌ای و سه‌بعدی
// امضای ورودی دقیقاً مثل نسخه قبلی است (NavGraph دست نمی‌خورد)
// ============================================================

data class MessageItemUi(
    val id: String,
    val title: String,
    val description: String
)

@Composable
fun MessageScreen(
    bottomBarActions: BottomBarActions,
) {
    val colors = LocalMadahiColors.current

    // TODO: اتصال به ViewModel پیام‌ها (getMessages از سرور)
    // فعلاً خالی است — بعداً از apiService.getMessages پر می‌شود
    val messages = remember { emptyList<MessageItemUi>() }

    var selectedMessage by remember { mutableStateOf<MessageItemUi?>(null) }

    BaseScreen(
        bottomBarActions = bottomBarActions,
        title = "پیام‌ها",
        subtitle = "",
        selectedBottomTab = BottomTab.MESSAGE,
        onHeaderBottonClicked = {},
    ) {

        if (messages.isEmpty()) {

            // ============ حالت خالی ============
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "✉️",
                    fontSize = 56.sp
                )

                Spacer(Modifier.height(18.dp))

                Text(
                    text = "پیامی وجود ندارد",
                    color = colors.textMuted,
                    fontSize = 16.sp
                )
            }

        } else {

            // ============ لیست پیام‌ها ============
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(messages) { message ->

                    GlassCard3D(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedMessage = message }) {

                        Column(
                            modifier = Modifier.padding(
                                horizontal = 22.dp,
                                vertical = 16.dp
                            )
                        ) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text = "📩",
                                    fontSize = 20.sp
                                )

                                Spacer(Modifier.width(12.dp))

                                Text(
                                    text = message.title,
                                    color = colors.textPrimary,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )

                                Text(
                                    text = "‹",
                                    color = colors.gold,
                                    fontSize = 20.sp
                                )
                            }

                            Spacer(Modifier.height(6.dp))

                            Text(
                                text = message.description,
                                color = colors.textMuted,
                                fontSize = 14.sp,
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }
    }

    // ============ دیالوگ نمایش کامل پیام ============
    selectedMessage?.let { message ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.45f))
                .clickable { selectedMessage = null })

        GlassCard(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = message.title,
                    color = colors.textPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = message.description,
                    color = colors.textMuted,
                    fontSize = 15.sp,
                    lineHeight = 28.sp
                )

                Spacer(Modifier.height(20.dp))

                Gold3DButton(
                    text = "بستن",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { selectedMessage = null })
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun MessageScreenPreview() {
    MadahiThemeGreen(darkTheme = true) {
        MessageScreen(bottomBarActions = BottomBarActions())
    }
}
