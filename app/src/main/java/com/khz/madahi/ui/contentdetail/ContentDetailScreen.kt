// ui/contentdetail/ContentDetailScreen.kt
package com.khz.madahi.ui.contentdetail

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.api.RetrofitClient
import com.khz.madahi.data.remote.repository.ContentRepository
import com.khz.madahi.models.Content
import com.khz.madahi.ui.audio.AddVoiceDialog
import com.khz.madahi.ui.audio.VoicePlayerCard
import com.khz.madahi.ui.components.Delete3DButton
import com.khz.madahi.ui.poems.PremiumDialog
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.components.GlassTextField
import com.khz.madahi.ui.components.Gold3DButton
import com.khz.madahi.ui.components.MadahiBackground
import com.khz.madahi.ui.components.Mini3DButton
import com.khz.madahi.ui.components.ThreeDButton
import com.khz.madahi.ui.components.TopTitleBar
import com.khz.madahi.ui.content.DeleteContentDialog
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.border
import com.khz.madahi.ui.theme.delete
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.surfaceGlass
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary
import com.khz.madahi.ui.theme.textSecondary

// ============================================================
// صفحه نمایش شعر — طراحی شیشه‌ای و سه‌بعدی
// ------------------------------------------------------------
// امکانات:
//  - مشاهده شعر با امکان تغییر اندازه فونت
//  - اشتراک‌گذاری
//  - علاقه‌مندی (قلب)
//  - ویرایش در همان صفحه (بدون دیالوگ)
//    با کلیک روی ✏️ فیلدها ظاهر می‌شوند و «ذخیره» تغییرات را اعمال می‌کند
// ============================================================

@Composable
fun ContentDetailScreen(
    content: Content?,
    onNavigateBack: () -> Unit,
    onFavoriteChanged: () -> Unit = {}
) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }

    // ============ اگر محتوا null بود ============
    if (content == null) {
        val colors = LocalMadahiColors.current
        MadahiBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                TopTitleBar(
                    title = "",
                    onBack = onNavigateBack
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "محتوا یافت نشد",
                        color = colors.textMuted,
                        fontSize = 18.sp
                    )
                }
            }
        }
        return
    }

    // ============ ViewModel ============
    val viewModel: ContentDetailViewModel = viewModel(
        factory = ContentDetailViewModelFactory(
            preferencesManager = PreferencesManager(context),
            contentRepository = ContentRepository(
                apiService = RetrofitClient.apiService,
                contentDao = AppDatabase.getInstance(context)
                    .contentDAO(),
                favoriteDao = AppDatabase.getInstance(context)
                    .favoriteDAO()
            ),
            appDatabase = AppDatabase.getInstance(context)
        )
    )

    // ✅ فقط یک بار برای هر محتوا
    LaunchedEffect(content.id) {
        viewModel.loadFavoriteStatus(content)
    }

    val favoriteState by viewModel.favoriteState.collectAsState()
    val uploadState by viewModel.uploadState.collectAsState()
    val audioUrlMessage by viewModel.audioUrlMessage.collectAsState()

    // ============ وضعیت حذف ============
    var showDeleteDialog by remember { mutableStateOf(false) }

    // ============ وضعیت دیالوگ پرو (برای افزودن ویس) ============
    var showPremiumDialog by remember { mutableStateOf(false) }

    // ============ وضعیت دیالوگ افزودن ویس (ضبط/انتخاب + پیش‌نمایش + ارسال) ============
    var showAddVoiceDialog by remember { mutableStateOf(false) }

    // ============ انتخاب فایل صوتی ============
    val audioPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            viewModel.uploadAudio(context, content, uri)
        }
    }

    fun requestAddAudio() {
        viewModel.ensurePremium { isPro ->
            if (isPro) {
                // دیالوگ جدید: ضبط از میکروفون یا انتخاب از حافظه + پیش‌نمایش + ارسال
                showAddVoiceDialog = true
            } else {
                showPremiumDialog = true
            }
        }
    }
    var deleteError by remember { mutableStateOf<String?>(null) }

    // ✅ محتوا به‌روز — بعد از آپلود موفق، audio_url تازه می‌شود و پلیر ویس نمایش داده می‌شود
    var currentContent by remember { mutableStateOf(content) }

    LaunchedEffect(content.id) {
        currentContent = content
    }

    LaunchedEffect(uploadState) {
        if (uploadState is UploadState.Success) {
            try {
                val fresh = AppDatabase.getInstance(context)
                    .contentDAO()
                    .getById(content.id)
                if (fresh != null) {
                    currentContent = fresh
                }
            } catch (_: Exception) {
            }
        }
    }

    // ============ اندازه فونت (ذخیره و بازیابی) ============
    var fontSize by remember {
        mutableFloatStateOf(preferencesManager.contentFontSize)
    }

    fun saveFontSize(size: Float) {
        preferencesManager.contentFontSize = size
        fontSize = size
    }

    // ============ متن نمایشی (پاک‌سازی تگ‌ها) ============
    val displayContent = currentContent.content.replace(
        "<p>",
        ""
    )
        .replace(
            "</p>",
            ""
        )
        .replace(
            "<br>",
            ""
        )
        .replace(
            "<br/>",
            ""
        )
        .replace(
            "<br />",
            ""
        )
        .trim()

    // ============ اشتراک‌گذاری ============
    val onShare = {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                """
                📖 ${currentContent.subject}
                ﷺ ${currentContent.answer}

                $displayContent

                ──────────────
                📱 دفتر مداحی
                """.trimIndent()
            )
            type = "text/plain"
        }
        context.startActivity(
            Intent.createChooser(
                shareIntent,
                "اشتراک‌ گذاری محتوا"
            )
        )
    }

    // ============ UI (stateless) ============
    ContentDetailScreenContent(
        content = currentContent,
        displayContent = displayContent,
        fontSize = fontSize,
        favoriteState = favoriteState,
        uploadState = uploadState,
        audioUrlMessage = audioUrlMessage,
        onFontSizeChange = { saveFontSize(it) },
        onToggleFavorite = {
            viewModel.toggleFavorite(content) {
                onFavoriteChanged()
            }
        },
        onShare = onShare,
        onNavigateBack = onNavigateBack,
        onDeleteClick = { showDeleteDialog = true },
        onAddAudioClick = { requestAddAudio() },
        onClearUploadError = { viewModel.clearUploadState() }
    )

    // ============ دیالوگ تأیید حذف ============
    if (showDeleteDialog) {
        DeleteContentDialog(
            content = content,
            onDelete = {
                showDeleteDialog = false
                viewModel.deleteContent(
                    content = content,
                    onSuccess = { onNavigateBack() },
                    onError = { msg -> deleteError = msg }
                )
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    // ============ دیالوگ نسخه پرو (برای افزودن ویس) ============
    if (showPremiumDialog) {
        PremiumDialog(
            onDismiss = { showPremiumDialog = false },
            onActivated = {
                showPremiumDialog = false
                // بعد از فعال‌سازی، مستقیم دیالوگ افزودن ویس را باز کن
                showAddVoiceDialog = true
            }
        )
    }

    // ============ دیالوگ افزودن ویس (ضبط/انتخاب + پیش‌نمایش + ارسال) ============
    if (showAddVoiceDialog) {

        AddVoiceDialog(
            content = content,
            onSend = { uri ->
                showAddVoiceDialog = false
                // آپلود با کد موجود ViewModel — وضعیتش توی پایین صفحه نمایش داده می‌شود
                viewModel.uploadAudio(context, content, uri)
            },
            onDismiss = { showAddVoiceDialog = false }
        )
    }

    // ============ خطای حذف ============
    deleteError?.let { message ->
        AlertDialog(
            onDismissRequest = { deleteError = null },
            title = { Text("❌ خطا") },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = { deleteError = null }) {
                    Text("تأیید")
                }
            }
        )
    }
}

// ============================================================
// UI خالص — بدون ViewModel (برای Preview و تست)
// ============================================================

@Composable
fun ContentDetailScreenContent(
    content: Content,
    displayContent: String,
    fontSize: Float,
    favoriteState: FavoriteState,
    uploadState: UploadState = UploadState.Idle,
    audioUrlMessage: String? = null,
    isReadOnly: Boolean = false,
    onFontSizeChange: (Float) -> Unit,
    onToggleFavorite: () -> Unit,
    onShare: () -> Unit,
    onNavigateBack: () -> Unit,
    onDeleteClick: () -> Unit,
    onAddAudioClick: () -> Unit = {},
    onClearUploadError: () -> Unit = {}
) {
    val colors = LocalMadahiColors.current

    // ============ state ویرایش (داخل همین صفحه) ============
    var isEditing by remember { mutableStateOf(true) }

    // فیلدهای ویرایش — مقدار اولیه از خود محتوا
    var editSubject by remember { mutableStateOf(content.subject) }
    var editAnswer by remember { mutableStateOf(content.answer) }
    var editContent by remember { mutableStateOf(displayContent) }

    // بازنشانی وقتی محتوا عوض شد
    LaunchedEffect(content.id) {
        editSubject = content.subject
        editAnswer = content.answer
        editContent = displayContent
        isEditing = false
    }

    fun startEditing() {
        editSubject = content.subject
        editAnswer = content.answer
        editContent = displayContent
        isEditing = true
    }

    MadahiBackground {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // ============ هدر ============
            if (!isEditing) {
                TopTitleBar(
                    title = content.subject.ifBlank { "" },
                    subTitle = content.answer.ifBlank { " " },
                    onBack = onNavigateBack
                )

                Spacer(Modifier.height(8.dp))

            }
            // ================================================
            // اگر در حالت ویرایش → فرم ویرایش
            // ================================================
            if (isEditing) {

                GlassCard3D(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .weight(1f)
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 22.dp,
                                vertical = 24.dp
                            )
                    ) {

                        Text(
                            text = "عنوان",
                            color = colors.textSecondary,
                            fontSize = 14.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        GlassTextField(
                            value = editSubject,
                            onValueChange = { editSubject = it },
                            minHeight = 52.dp
                        )

                        Spacer(Modifier.height(14.dp))

                        Text(
                            text = "جواب (اختیاری)",
                            color = colors.textSecondary,
                            fontSize = 14.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        GlassTextField(
                            value = editAnswer,
                            onValueChange = { editAnswer = it },
                            minHeight = 52.dp
                        )

                        Spacer(Modifier.height(14.dp))

                        Text(
                            text = "متن",
                            color = colors.textSecondary,
                            fontSize = 14.sp
                        )
                        Spacer(Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    colors.surfaceGlass.copy(alpha = 0.55f),
                                    RoundedCornerShape(18.dp)
                                )
                                .border(
                                    1.dp,
                                    colors.border,
                                    RoundedCornerShape(18.dp)
                                )
                                .weight(1f),
                        ) {

                            BasicTextField(
                                value = editContent,
                                onValueChange = {
                                    editContent = it
                                },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                                    .background(
                                        colors.surfaceGlass.copy(alpha = 0.55f),
                                        RoundedCornerShape(18.dp)
                                    )
                                    .padding(14.dp),
                                textStyle = TextStyle(
                                    color = colors.textPrimary,
                                    fontSize = 17.sp,
                                    lineHeight = 30.sp,
                                    textAlign = TextAlign.Center
                                )
                            )

                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // ============ دکمه‌های حذف/انصراف/ذخیره ============
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Delete3DButton(
                        text = "حذف",
                        modifier = Modifier.weight(1f),
                        onClick = onDeleteClick
                    )

                    Gold3DButton(
                        text = "انصراف",
                        modifier = Modifier.weight(1f),
                        onClick = { isEditing = false })

                    ThreeDButton(
                        text = "ذخیره",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            // ⚠️ اینجا باید به ViewModel وصل شود:
                            // viewModel.editContent() یا متد مشابه
                            isEditing = false
                        })
                }

            } else {

                // ================================================
                // حالت نمایش — متن شعر
                // ================================================

                GlassCard3D(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .weight(1f)
                ) {

                    Text(
                        text = displayContent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(
                                horizontal = 22.dp,
                                vertical = 26.dp
                            ),
                        color = colors.textPrimary,
                        fontSize = fontSize.sp,
                        lineHeight = (fontSize * 1.8f).sp,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(16.dp))

                // ============ 🎧 پلیر ویس — فقط اگر شعر ویس دارد ============
                if (!content.audioUrl.isNullOrBlank()) {
                    VoicePlayerCard(
                        audioUrl = content.audioUrl!!
                    )
                    Spacer(Modifier.height(10.dp))
                }

                // ============ نوار دکمه‌ها — پایین صفحه ============
                GlassCard3D(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 24.dp,
                            end = 24.dp,
                            bottom = 12.dp
                        )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp,
                                vertical = 12.dp
                            ),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // اشتراک‌گذاری
                        Mini3DButton(
                            imageVector = Icons.Default.Share,
                            tint = colors.gold,
                            onClick = onShare,
                        )

                        // کاهش فونت
                        Mini3DButton(
                            imageVector = Icons.Default.ZoomOut,
                            tint = colors.gold,
                            onClick = {
                                if (fontSize > 12f) onFontSizeChange(fontSize - 2f)
                            },
                        )

                        // افزایش فونت
                        Mini3DButton(
                            imageVector = Icons.Default.ZoomIn,
                            tint = colors.gold,
                            onClick = {
                                if (fontSize < 30f) onFontSizeChange(fontSize + 2f)
                            },
                        )

                        // ✏️ ویرایش — ورود به حالت ویرایش در همین صفحه (فقط دفترچه)
                        if (!isReadOnly) {
                            Mini3DButton(
                                imageVector = Icons.Default.Edit,
                                tint = colors.gold,
                                onClick = { startEditing() },
                            )
                        }

                        // 🎤 افزودن ویس/سبک — ویژه نسخه پرو (فقط دفترچه)
                        // (اگر ویس داشته باشد رنگ عادی، وگرنه طلایی = قابل اضافه کردن)
                        if (!isReadOnly) {
                            Mini3DButton(
                                imageVector = if (uploadState is UploadState.Uploading) {
                                    Icons.Default.Stop
                                } else {
                                    Icons.Default.Mic
                                },
                                tint = if (content.audioUrl.isNullOrBlank()) {
                                    colors.gold
                                } else {
                                    colors.textPrimary
                                },
                                onClick = onAddAudioClick
                            )
                        }

                        // علاقه‌مندی (قلب — با رنگ پویا)
                        GlassCard3D(
                            modifier = Modifier
                                .size(54.dp)
                                .clickable(onClick = onToggleFavorite)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (favoriteState is FavoriteState.Favorite) {
                                        Icons.Default.Favorite
                                    } else {
                                        Icons.Default.FavoriteBorder
                                    },
                                    contentDescription = null,
                                    tint = if (favoriteState is FavoriteState.Favorite) {
                                        colors.delete
                                    } else {
                                        colors.gold
                                    },
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                    }
                }

                // ============ وضعیت آپلود ویس ============
                when (uploadState) {
                    is UploadState.Uploading -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = colors.gold,
                                strokeWidth = 2.dp
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = "در حال آپلود ویس…",
                                color = colors.textMuted,
                                fontSize = 13.sp
                            )
                        }
                    }

                    is UploadState.Error -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onClearUploadError() }
                                .padding(horizontal = 24.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "⚠️ ${(uploadState as UploadState.Error).message}",
                                color = colors.delete,
                                fontSize = 13.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "بستن",
                                color = colors.textMuted,
                                fontSize = 12.sp
                            )
                        }
                    }

                    else -> { /* Idle / Success */ }
                }

                // ============ نتیجه‌ی بررسی لینک فایل ============
                if (!audioUrlMessage.isNullOrBlank()) {
                    Text(
                        text = audioUrlMessage!!,
                        color = if (audioUrlMessage!!.startsWith("⚠️")) {
                            colors.delete
                        } else {
                            colors.textMuted
                        },
                        fontSize = 12.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(Modifier.height(6.dp))
        }
    }
}

// ============================================================
// Preview — با داده نمونه (بدون ViewModel → همیشه رندر می‌شود)
// ============================================================
//
//@Preview(showBackground = false)
//@Composable
//private fun ContentDetailScreenPreview() {
//    MadahiThemeGreen(darkTheme = true) {
//        ContentDetailScreenContent(
//            content = Content(
//                idContent = 0,
//                id = 1,
//                categoryId = 1,
//                userId = 1,
//                answer = "ای اهل حرم",
//                content = "<p>متن نمونه برای نمایش در صفحه جزئیات محتوا. این متن برای تست و نمایش ظاهر صفحه استفاده می‌شود.</p>",
//                subject = "نوحه‌های محرم",
//                contentType = "0"
//            ),
//            displayContent = "متن نمونه برای نمایش در صفحه جزئیات محتوا. این متن برای تست و نمایش ظاهر صفحه استفاده می‌شود.",
//            fontSize = 16f,
//            favoriteState = FavoriteState.NotFavorite,
//            onFontSizeChange = {},
//            onToggleFavorite = {},
//            onShare = {},
//            onNavigateBack = {})
//    }
//}

@Preview(showBackground = false)
@Composable
private fun ContentDetailScreenPreviewLight() {
    MadahiThemeGreen(darkTheme = false) {
        ContentDetailScreenContent(
            content = Content(
                idContent = 0,
                id = 2,
                categoryId = 1,
                userId = 1,
                answer = "روضه حضرت زهرا",
                content = "<p>متن نمونه روضه...</p>",
                subject = "روضه‌های فاطمیه",
                contentType = "1"
            ),
            displayContent = "متن نمونه روضه...",
            fontSize = 18f,
            favoriteState = FavoriteState.Favorite,
            onFontSizeChange = {},
            onToggleFavorite = {},
            onShare = {},
            onNavigateBack = {},
            onDeleteClick = {})
    }
}
