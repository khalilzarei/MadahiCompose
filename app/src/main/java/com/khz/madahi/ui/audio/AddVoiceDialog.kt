package com.khz.madahi.ui.audio

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.khz.madahi.models.Content
import com.khz.madahi.ui.components.Delete3DButton
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.components.GlassIconButton
import com.khz.madahi.ui.components.Gold3DButton
import com.khz.madahi.ui.components.Mini3DButton
import com.khz.madahi.ui.components.ThreeDButton
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.border
import com.khz.madahi.ui.theme.delete
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.goldLight
import com.khz.madahi.ui.theme.primaryDark
import com.khz.madahi.ui.theme.surfaceGlass
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

private enum class VoiceStep {
    CHOOSE,
    RECORDING,
    PREPARE,
    PREVIEW
}

@Composable
fun AddVoiceDialog(
    content: Content,
    onSend: (Uri) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val colors = LocalMadahiColors.current
    val scope = rememberCoroutineScope()

    var step by remember { mutableStateOf(VoiceStep.CHOOSE) }
    var pendingUri by remember { mutableStateOf<Uri?>(null) }
    var elapsedMs by remember { mutableLongStateOf(0L) }
    var recordStartAt by remember { mutableLongStateOf(0L) }
    var isPlaying by remember { mutableStateOf(false) }
    var isPreparingAudio by remember { mutableStateOf(false) }
    var durationMs by remember { mutableLongStateOf(0L) }
    var fileSize by remember { mutableLongStateOf(-1L) }
    var trimStartMs by remember { mutableLongStateOf(0L) }
    var trimEndMs by remember { mutableLongStateOf(0L) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val recorder = remember { AudioRecorder(context) }
    val player = remember {
        ExoPlayer.Builder(context)
            .build()
    }

    val playerListener = remember {
        object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    isPreparingAudio = false
                    val d = player.duration
                    if (d >= 0) {
                        durationMs = d
                        // اگر هنوز trimEnd بهصورت خودکار تنظیم نشده (مثلاً مقدار اولیه صفر است)
                        if (trimEndMs <= 0L && durationMs > 0L) {
                            trimEndMs = if (fileSize > AudioUtils.MAX_FILE_SIZE_BYTES) {
                                // برای فایلهای حجیم، خودکار نقطهای را انتخاب کن که حجم نهایی زیر ۱ مگابایت شود
                                AudioUtils.autoFitEndMs(
                                    durationMs,
                                    fileSize
                                )
                            } else {
                                minOf(
                                    durationMs,
                                    AudioUtils.MAX_DURATION_MS
                                )
                            }
                        }
                    }
                }
            }

            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }

            override fun onPlayerError(error: PlaybackException) {
                isPreparingAudio = false
                isPlaying = false
                errorMsg = "پخش فایل ممکن نبود (${error.errorCodeName})"
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            if (recorder.isRecording) recorder.cancel()
            try {
                player.removeListener(playerListener)
            } catch (_: Exception) {
            }
            try {
                player.release()
            } catch (_: Exception) {
            }
        }
    }

    fun acceptUri(uri: Uri): Boolean {
        pendingUri = uri
        durationMs = AudioUtils.durationMs(
            context,
            uri
        )   // فوراً duration را خواندهایم
        fileSize = AudioUtils.fileSizeBytes(
            context,
            uri
        )
        trimStartMs = 0L

        // اگر حجم از حد مجاز بیشتر بود، بهصورت خودکار نقطهای را انتخاب کن که زیر ۱ مگابایت باشد
        trimEndMs = AudioUtils.autoFitEndMs(
            durationMs,
            fileSize
        )
        step = VoiceStep.PREVIEW
        return true
    }

    fun startRecording() {
        errorMsg = null
        if (recorder.start()) {
            recordStartAt = System.currentTimeMillis()
            elapsedMs = 0L
            step = VoiceStep.RECORDING
        } else {
            errorMsg = "شروع ضبط ممکن نبود — دوباره تلاش کن"
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            startRecording()
        } else {
            errorMsg = "برای ضبط، اجازه‌ی میکروفن را از تنظیمات بده"
        }
    }

    fun onRecordClick() {
        if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            startRecording()
        } else {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    fun stopRecording() {
        val uri = recorder.stop()
        if (uri == null) {
            errorMsg = "ضبط خالی یا خیلی کوتاه بود — دوباره تلاش کن"
            step = VoiceStep.CHOOSE
            return
        }
        val recDuration = AudioUtils.durationMs(
            context,
            uri
        )
        val expectedMs = System.currentTimeMillis() - recordStartAt
        if (recDuration <= 1000L || (expectedMs > 3000L && recDuration * 2 < expectedMs)) {
            errorMsg = "ضبط ناقص به نظر می‌رسد (${recDuration / 1000} ثانیه از ${expectedMs / 1000} ثانیه) — دوباره تلاش کن"
            step = VoiceStep.CHOOSE
            return
        }
        if (!acceptUri(uri)) {
            step = VoiceStep.CHOOSE
        }
    }

    LaunchedEffect(step) {
        if (step == VoiceStep.RECORDING) {
            while (recorder.isRecording) {
                val el = System.currentTimeMillis() - recordStartAt
                elapsedMs = if (el < AudioRecorder.MAX_DURATION_MS) {
                    el
                } else {
                    AudioRecorder.MAX_DURATION_MS
                }
                if (el >= AudioRecorder.MAX_DURATION_MS) {
                    stopRecording()
                    break
                }
                delay(100)
            }
        }
    }

    val pickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {
            }
            errorMsg = null
            if (!acceptUri(uri)) {
                step = VoiceStep.CHOOSE
            }
        }
    }

    LaunchedEffect(Unit) {
        try {
            player.addListener(playerListener)
        } catch (_: Exception) {
        }
    }

    LaunchedEffect(
        step,
        pendingUri
    ) {
        if (step != VoiceStep.PREVIEW) return@LaunchedEffect
        val u = pendingUri
                ?: return@LaunchedEffect

        try {
            player.stop()
        } catch (_: Exception) {
        }
        isPlaying = false
        isPreparingAudio = true
        durationMs = 0L

        try {
            player.setMediaItem(MediaItem.fromUri(u))
            player.prepare()
            player.play()
        } catch (e: Exception) {
            isPreparingAudio = false
            errorMsg = "پخش فایل ممکن نبود"
        }
    }

    fun togglePlay() {
        if (isPreparingAudio) return
        try {
            if (isPlaying) player.pause() else player.play()
        } catch (_: Exception) {
        }
    }

    val needsTrim = (trimStartMs > 0L) || (trimEndMs in 1 until durationMs) || (durationMs > AudioUtils.MAX_DURATION_MS) || (fileSize > AudioUtils.MAX_FILE_SIZE_BYTES)

    fun onTrimStartChange(frac: Float) {
        if (durationMs <= 0L) return
        val maxStart = maxOf(
            0L,
            durationMs - AudioUtils.MAX_DURATION_MS
        )
        val s = (frac * maxStart).toLong()
            .coerceIn(
                0L,
                maxStart
            )
        trimStartMs = s
        val hi = minOf(
            durationMs,
            s + AudioUtils.MAX_DURATION_MS
        )
        if (trimEndMs > hi) trimEndMs = hi
    }

    fun onTrimEndChange(frac: Float) {
        val lo = trimStartMs
        val hi = minOf(
            durationMs,
            lo + AudioUtils.MAX_DURATION_MS
        )
        if (hi <= lo) return
        trimEndMs = (lo + frac * (hi - lo)).toLong()
            .coerceIn(
                lo,
                hi
            )
    }

    val startFraction = if (durationMs > 0L) {
        val maxStart = maxOf(
            1L,
            durationMs - AudioUtils.MAX_DURATION_MS
        )
        (trimStartMs.toFloat() / maxStart).coerceIn(
            0f,
            1f
        )
    } else {
        0f
    }
    val endFraction = if (needsTrim) {
        val lo = trimStartMs
        val hi = minOf(
            durationMs,
            lo + AudioUtils.MAX_DURATION_MS
        )
        if (hi > lo) ((trimEndMs - lo).toFloat() / (hi - lo)).coerceIn(
            0f,
            1f
        ) else 1f
    } else {
        0f
    }

    fun doSend() {
        val u = pendingUri
                ?: return
        try {
            player.pause()
        } catch (_: Exception) {
        }
        isPlaying = false

        if (!needsTrim && fileSize <= AudioUtils.MAX_FILE_SIZE_BYTES && durationMs <= AudioUtils.MAX_DURATION_MS) {
            onSend(u)
            return
        }

        step = VoiceStep.PREPARE
        scope.launch(Dispatchers.IO) {
            val safeEnd = if (trimEndMs > trimStartMs) trimEndMs else minOf(
                durationMs,
                trimStartMs + AudioUtils.MAX_DURATION_MS
            )
            val trimmed = AudioUtils.trimRange(
                context,
                u,
                trimStartMs,
                safeEnd,
                durationMs
            )
            val size = AudioUtils.fileSizeBytes(
                context,
                trimmed
            )

            withContext(Dispatchers.Main) {
                if (size > 0 && size <= AudioUtils.MAX_FILE_SIZE_BYTES) {
                    onSend(trimmed)
                } else if (trimmed == u) {
                    val withinLimits = durationMs <= AudioUtils.MAX_DURATION_MS && fileSize <= AudioUtils.MAX_FILE_SIZE_BYTES
                    if (withinLimits) {
                        onSend(u)
                    } else {
                        val detail = AudioUtils.lastError
                        errorMsg = "کات این فایل ممکن نبود" + (detail?.let { " — $it" }
                                ?: "") + " — فایل MP3 یا کوتاه‌تری انتخاب کن"
                        step = VoiceStep.PREVIEW
                    }
                } else {
                    errorMsg = "حجم فایل حتی بعد از کات بیشتر از ۱ مگابایت است. برای کاهش حجم، نقطهی «پایان» را کمتر از ${
                        AudioUtils.autoFitEndMs(
                            durationMs,
                            fileSize
                        ) / 1000
                    } ثانیه تنظیم کن."
                    step = VoiceStep.PREVIEW
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
    ) {
        GlassCard3D(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = when (step) {
                                VoiceStep.CHOOSE    -> "افزودن ویس به شعر"
                                VoiceStep.RECORDING -> "در حال ضبط…"
                                VoiceStep.PREPARE   -> "آماده‌سازی صدا…"
                                VoiceStep.PREVIEW   -> "پیش‌نمایش ویس"
                            },
                            color = colors.textPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = content.subject,
                            color = colors.gold,
                            fontSize = 12.sp
                        )
                    }
                    GlassIconButton(
                        imageVector = Icons.Default.Close,
                        onClick = onDismiss,
                        tint = colors.textPrimary,
                        size = 42.dp
                    )
                }

                Spacer(Modifier.height(16.dp))

                when (step) {
                    VoiceStep.CHOOSE    -> {
                        Text(
                            text = "ویس را چطور اضافه می‌کنی؟",
                            color = colors.textMuted,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        GlassCard3D(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onRecordClick() }) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                VoiceOptionIcon(Icons.Default.Mic)
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(
                                        text = "ضبط ویس",
                                        color = colors.textPrimary,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        text = "با میکروفون ضبط می‌کنی (حداکثر ۱ دقیقه)",
                                        color = colors.textMuted,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        GlassCard3D(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    pickerLauncher.launch(arrayOf("audio/*"))
                                }) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                VoiceOptionIcon(Icons.Default.InsertDriveFile)
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(
                                        text = "انتخاب از حافظه",
                                        color = colors.textPrimary,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        text = "فایل صوتی انتخاب می‌کنی (بعدش محدوده‌ی دلخواه را کات می‌کنی)",
                                        color = colors.textMuted,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }

                    VoiceStep.RECORDING -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(
                                                colors.goldLight,
                                                colors.gold
                                            )
                                        ),
                                        CircleShape
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = Color.White.copy(alpha = 0.3f),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = null,
                                    tint = colors.primaryDark,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Spacer(Modifier.height(18.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = formatDuration(elapsedMs),
                                    color = colors.textPrimary,
                                    fontSize = 34.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "/ 1:00",
                                    color = colors.textMuted,
                                    fontSize = 16.sp
                                )
                            }

                            Spacer(Modifier.height(14.dp))

                            val fraction = (elapsedMs.toFloat() / AudioRecorder.MAX_DURATION_MS).coerceIn(
                                0f,
                                1f
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .background(
                                        colors.surfaceGlass.copy(alpha = 0.6f),
                                        RoundedCornerShape(50)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = colors.border,
                                        shape = RoundedCornerShape(50)
                                    )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(fraction)
                                        .fillMaxHeight()
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(
                                                    colors.gold,
                                                    colors.goldLight
                                                )
                                            ),
                                            RoundedCornerShape(50)
                                        )
                                )
                            }

                            Spacer(Modifier.height(20.dp))

                            Gold3DButton(
                                text = "توقف و ادامه",
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { stopRecording() })

                            Spacer(Modifier.height(10.dp))

                            Delete3DButton(
                                text = "لغو ضبط",
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    recorder.cancel()
                                    step = VoiceStep.CHOOSE
                                })
                        }
                    }

                    VoiceStep.PREPARE   -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = "در حال آماده‌سازی صدا… (کات محدوده‌ی انتخابی)",
                                color = colors.textMuted,
                                fontSize = 14.sp
                            )
                        }
                    }

                    VoiceStep.PREVIEW   -> {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .background(
                                            Brush.radialGradient(
                                                listOf(
                                                    colors.goldLight,
                                                    colors.gold
                                                )
                                            ),
                                            CircleShape
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = Color.White.copy(alpha = 0.3f),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isPreparingAudio) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(20.dp),
                                            color = colors.primaryDark,
                                            strokeWidth = 2.dp
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Mic,
                                            contentDescription = null,
                                            tint = colors.primaryDark,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }

                                Spacer(Modifier.width(12.dp))

                                Mini3DButton(
                                    imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                                    tint = if (isPreparingAudio) colors.textMuted else colors.gold,
                                    onClick = { togglePlay() })

                                Spacer(Modifier.width(14.dp))

                                Column(Modifier.weight(1f)) {
                                    Text(
                                        text = "🎧 ویس شعر",
                                        color = colors.textPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = if (isPreparingAudio) {
                                            "در حال آماده‌سازی…"
                                        } else if (durationMs > 0L) {
                                            "مدت کل: " + formatDuration(durationMs)
                                        } else {
                                            "—"
                                        },
                                        color = colors.textMuted,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            if (needsTrim) {
                                Spacer(Modifier.height(14.dp))

                                GlassCard3D {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp)
                                    ) {
                                        Text(
                                            text = "✂️ محدوده‌ی کات (حداکثر ۱ دقیقه)",
                                            color = colors.gold,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )

                                        if (fileSize > AudioUtils.MAX_FILE_SIZE_BYTES) {
                                            Spacer(Modifier.height(6.dp))
                                            Text(
                                                text = "حجم فایل از ۱ مگابایت بیشتر است — محدوده‌ی کوتاه‌تری انتخاب کن تا حجم کم شود",
                                                color = colors.delete,
                                                fontSize = 12.sp
                                            )
                                        }

                                        Spacer(Modifier.height(10.dp))

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "شروع",
                                                color = colors.textPrimary,
                                                fontSize = 13.sp,
                                                modifier = Modifier.width(44.dp)
                                            )
                                            Slider(
                                                value = startFraction,
                                                onValueChange = { onTrimStartChange(it) },
                                                enabled = !isPreparingAudio,
                                                colors = SliderDefaults.colors(
                                                    thumbColor = colors.gold,
                                                    activeTrackColor = colors.gold,
                                                    inactiveTrackColor = colors.surfaceGlass
                                                ),
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height(28.dp)
                                            )
                                            Text(
                                                text = formatDuration(trimStartMs),
                                                color = colors.textMuted,
                                                fontSize = 12.sp,
                                                modifier = Modifier.width(44.dp)
                                            )
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "پایان",
                                                color = colors.textPrimary,
                                                fontSize = 13.sp,
                                                modifier = Modifier.width(44.dp)
                                            )
                                            Slider(
                                                value = endFraction,
                                                onValueChange = { onTrimEndChange(it) },
                                                enabled = !isPreparingAudio,
                                                colors = SliderDefaults.colors(
                                                    thumbColor = colors.gold,
                                                    activeTrackColor = colors.goldLight,
                                                    inactiveTrackColor = colors.surfaceGlass
                                                ),
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height(28.dp)
                                            )
                                            Text(
                                                text = formatDuration(trimEndMs),
                                                color = colors.textMuted,
                                                fontSize = 12.sp,
                                                modifier = Modifier.width(44.dp)
                                            )
                                        }

                                        Spacer(Modifier.height(6.dp))

                                        Text(
                                            text = "مدت خروجی: " + formatDuration(trimEndMs - trimStartMs),
                                            color = colors.textMuted,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }

                            Spacer(Modifier.height(18.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                ThreeDButton(
                                    text = "انتخاب دوباره",
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        try {
                                            player.pause()
                                        } catch (_: Exception) {
                                        }
                                        isPlaying = false
                                        pendingUri = null
                                        step = VoiceStep.CHOOSE
                                    })
                                Gold3DButton(
                                    text = "ارسال",
                                    modifier = Modifier.weight(1f),
                                    onClick = { doSend() })
                            }
                        }
                    }
                }

                errorMsg?.let { msg ->
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "⚠️ $msg",
                        color = colors.delete,
                        fontSize = 13.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun VoiceOptionIcon(imageVector: androidx.compose.ui.graphics.vector.ImageVector) {
    val colors = LocalMadahiColors.current
    Box(
        modifier = Modifier
            .size(44.dp)
            .background(
                Brush.radialGradient(
                    listOf(
                        colors.goldLight,
                        colors.gold
                    )
                ),
                CircleShape
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.3f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = colors.primaryDark,
            modifier = Modifier.size(24.dp)
        )
    }
}

private fun formatDuration(ms: Long): String {
    val totalSec = (ms / 1000L).toInt()
    return String.format(
        Locale.US,
        "%d:%02d",
        totalSec / 60,
        totalSec % 60
    )
}
