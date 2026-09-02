// ui/contentdetail/ContentDetailScreen.kt
package com.khz.madahi.ui.contentdetail

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.api.RetrofitClient
import com.khz.madahi.data.remote.repository.ContentRepository
import com.khz.madahi.models.Content
import com.khz.madahi.ui.audio.AddVoiceDialog
import com.khz.madahi.ui.audio.VoicePlayerCard
import com.khz.madahi.ui.components.*
import com.khz.madahi.ui.content.DeleteContentDialog
import com.khz.madahi.ui.poems.PremiumDialog
import com.khz.madahi.ui.theme.*
import kotlinx.coroutines.launch

// ============================================================
// صفحه نمایش شعر — طراحی شیشهای و سهبعدی
// ------------------------------------------------------------
// امکانات:
//  - مشاهده شعر با امکان تغییر اندازه فونت
//  - اشتراکگذاری
//  - علاقهمندی (قلب)
//  - ویرایش در همان صفحه (بدون دیالوگ)
//  - افزودن ویس (ضبط/انتخاب) + آپلود با دیالوگ مسدودکننده
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
                contentDao = AppDatabase.getInstance(context).contentDAO(),
                favoriteDao = AppDatabase.getInstance(context).favoriteDAO()
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

    // ============ وضعیت دیالوگ افزودن ویس ============
    var showAddVoiceDialog by remember { mutableStateOf(false) }

    // ============ انتخاب فایل صوتی (برای حالت مستقیم) ============
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
                showAddVoiceDialog = true
            } else {
                showPremiumDialog = true
            }
        }
    }

    var deleteError by remember { mutableStateOf<String?>(null) }

    // ✅ محتوا بهروز — بعد از آپلود موفق
    var currentContent by remember { mutableStateOf(content) }

    LaunchedEffect(content.id) {
        currentContent = content
    }

    LaunchedEffect(uploadState) {
        if (uploadState is UploadState.Success) {
            // ✅ به‌روزرسانی مستقیم از پاسخ سرور (بدون نیاز به خواندن مجدد از دیتابیس)
            val newAudioUrl = (uploadState as UploadState.Success).audioUrl
            if (!newAudioUrl.isNullOrBlank()) {
                currentContent = currentContent.copy(audioUrl = newAudioUrl)
            }

            // ✅ به‌روزرسانی دیتابیس محلی به‌عنوان fallback
            try {
                val fresh = AppDatabase.getInstance(context)
                    .contentDAO()
                    .getById(content.id)
                if (fresh != null && !fresh.audioUrl.isNullOrBlank()) {
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

    // ============ متن نمایشی ============
    val displayContent = currentContent.content.replace(
        "<p>", ""
    ).replace("</p>", "").replace("<br>", "").replace("<br/>", "").replace("<br />", "").trim()

    // ============ اشتراکگذاری ============
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
        context.startActivity(Intent.createChooser(shareIntent, "اشتراک گذاری محتوا"))
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
            viewModel.toggleFavorite(content) { onFavoriteChanged() }
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

    // ============ دیالوگ نسخه پرو ============
    if (showPremiumDialog) {
        PremiumDialog(
            onDismiss = { showPremiumDialog = false },
            onActivated = {
                showPremiumDialog = false
                showAddVoiceDialog = true
            }
        )
    }

    // ============ دیالوگ افزودن ویس ============
    if (showAddVoiceDialog) {
        AddVoiceDialog(
            content = content,
            onSend = { uri ->
                showAddVoiceDialog = false
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
                TextButton(onClick = { deleteError = null }) { Text("تأیید") }
            }
        )
    }

    // ============ دیالوگ آپلود مسدودکننده ============
    if (uploadState is UploadState.Uploading) {
        UploadingDialog()
    }
}

// ============================================================
// UI خالص — بدون ViewModel
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

    // ============ state ویرایش ============
    var isEditing by remember { mutableStateOf(true) }
    var editSubject by remember { mutableStateOf(content.subject) }
    var editAnswer by remember { mutableStateOf(content.answer) }
    var editContent by remember { mutableStateOf(displayContent) }

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
        Column(modifier = Modifier.fillMaxSize()) {
            if (!isEditing) {
                TopTitleBar(
                    title = content.subject.ifBlank { "" },
                    subTitle = content.answer.ifBlank { " " },
                    onBack = onNavigateBack
                )
                Spacer(Modifier.height(8.dp))
            }

            if (isEditing) {
                // ===== حالت ویرایش =====
                GlassCard3D(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .weight(1f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 22.dp, vertical = 24.dp)
                    ) {
                        Text("عنوان", color = colors.textSecondary, fontSize = 14.sp)
                        Spacer(Modifier.height(8.dp))
                        GlassTextField(
                            value = editSubject,
                            onValueChange = { editSubject = it },
                            minHeight = 52.dp
                        )

                        Spacer(Modifier.height(14.dp))
                        Text("جواب (اختیاری)", color = colors.textSecondary, fontSize = 14.sp)
                        Spacer(Modifier.height(8.dp))
                        GlassTextField(
                            value = editAnswer,
                            onValueChange = { editAnswer = it },
                            minHeight = 52.dp
                        )

                        Spacer(Modifier.height(14.dp))
                        Text("متن", color = colors.textSecondary, fontSize = 14.sp)
                        Spacer(Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(colors.surfaceGlass.copy(alpha = 0.55f), RoundedCornerShape(18.dp))
                                .border(1.dp, colors.border, RoundedCornerShape(18.dp))
                                .weight(1f)
                        ) {
                            BasicTextField(
                                value = editContent,
                                onValueChange = { editContent = it },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                                    .background(colors.surfaceGlass.copy(alpha = 0.55f), RoundedCornerShape(18.dp))
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
                        onClick = { isEditing = false }
                    )
                    ThreeDButton(
                        text = "ذخیره",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            // اینجا باید به ViewModel وصل شود
                            isEditing = false
                        }
                    )
                }

            } else {
                // ===== حالت نمایش =====
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
                            .padding(horizontal = 22.dp, vertical = 26.dp),
                        color = colors.textPrimary,
                        fontSize = fontSize.sp,
                        lineHeight = (fontSize * 1.8f).sp,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(16.dp))

                // ============ 🎧 پلیر ویس ============
                if (!content.audioUrl.isNullOrBlank()) {
                    VoicePlayerCard(audioUrl = content.audioUrl!!)
                    Spacer(Modifier.height(10.dp))
                }

                // ============ نوار دکمهها ============
                GlassCard3D(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Mini3DButton(
                            imageVector = Icons.Default.Share,
                            tint = colors.gold,
                            onClick = onShare,
                        )
                        Mini3DButton(
                            imageVector = Icons.Default.ZoomOut,
                            tint = colors.gold,
                            onClick = { if (fontSize > 12f) onFontSizeChange(fontSize - 2f) },
                        )
                        Mini3DButton(
                            imageVector = Icons.Default.ZoomIn,
                            tint = colors.gold,
                            onClick = { if (fontSize < 30f) onFontSizeChange(fontSize + 2f) },
                        )
                        if (!isReadOnly) {
                            Mini3DButton(
                                imageVector = Icons.Default.Edit,
                                tint = colors.gold,
                                onClick = { startEditing() },
                            )
                        }
                        if (!isReadOnly) {
                            Mini3DButton(
                                imageVector = if (uploadState is UploadState.Uploading) {
                                    Icons.Default.Stop
                                } else {
                                    Icons.Default.Mic
                                },
                                tint = if (content.audioUrl.isNullOrBlank()) colors.gold else colors.textPrimary,
                                onClick = onAddAudioClick
                            )
                        }
                        GlassCard3D(
                            modifier = Modifier
                                .size(54.dp)
                                .clickable(onClick = onToggleFavorite)
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (favoriteState is FavoriteState.Favorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = null,
                                    tint = if (favoriteState is FavoriteState.Favorite) colors.delete else colors.gold,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                    }
                }

                // ============ وضعیت آپلود ویس — به دیالوگ منتقل شد ============
                // (بخش `when (uploadState)` که نوار کوچک بود حذف شد)

                // ============ نتیجهی بررسی لینک فایل ============
                if (!audioUrlMessage.isNullOrBlank()) {
                    Text(
                        text = audioUrlMessage!!,
                        color = if (audioUrlMessage!!.startsWith("⚠️")) colors.delete else colors.textMuted,
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
// دیالوگ آپلود مسدودکننده
// ============================================================
@Composable
private fun UploadingDialog() {
    val colors = LocalMadahiColors.current

    Dialog(
        onDismissRequest = { /* هیچ کاری نکن */ },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.surfaceGlass.copy(alpha = 0.9f)),
            contentAlignment = Alignment.Center
        ) {
            GlassCard3D(modifier = Modifier.padding(32.dp)) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = colors.gold)
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
}

// ============================================================
// Preview
// ============================================================
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
                subject = "روضه های فاطمیه",
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