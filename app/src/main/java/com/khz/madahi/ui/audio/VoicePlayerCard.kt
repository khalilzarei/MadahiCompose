// ui/audio/VoicePlayerCard.kt
package com.khz.madahi.ui.audio

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.khz.madahi.ui.components.GlassCard3D
import com.khz.madahi.ui.components.Mini3DButton
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.delete
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.goldLight
import com.khz.madahi.ui.theme.primaryDark
import com.khz.madahi.ui.theme.surfaceGlass
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

/**
 * 🎧 پلیر ویسِ موجود شعر — با ExoPlayer (Media3)
 * ------------------------------------------------------------
 * مستقل از MediaPlayer سیستمی / لایه‌ی Rockchip:
 *  - دکمه پخش/توقف
 *  - اسلایدر جلو/عقب
 * صدا از لینک سرور استریم می‌شود
 */
@Composable
fun VoicePlayerCard(
    audioUrl: String
) {
    val context = LocalContext.current
    val colors = LocalMadahiColors.current

    // ============ ExoPlayer ============
    val player = remember {
        ExoPlayer.Builder(context)
            .build()
    }

    var isReady by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var durationMs by remember { mutableLongStateOf(0L) }
    var positionMs by remember { mutableLongStateOf(0L) }
    var loadError by remember { mutableStateOf<String?>(null) }

    // ============ Listener (روی main thread فراخوانی می‌شود) ============
    val listener = remember {
        object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    isReady = true
                    val d = player.duration
                    if (d >= 0) durationMs = d.toLong()
                }
                if (playbackState == Player.STATE_ENDED) {
                    positionMs = 0L
                    try {
                        player.seekTo(0)
                    } catch (_: Exception) {
                    }
                }
            }

            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
                if (!playing) positionMs = player.currentPosition.toLong()
            }

            fun onPlayerCompleted() {
                positionMs = 0L
                try {
                    player.seekTo(0)
                } catch (_: Exception) {
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                loadError = "پخش صدا ممکن نبود (${error.errorCodeName})"
            }
        }
    }

    // ============ اضافه‌کردن Listener فقط یک‌بار ============
    LaunchedEffect(Unit) {
        try {
            player.addListener(listener)
        } catch (_: Exception) {
        }
    }

    // ============ لود و پخش خودکار ============
    LaunchedEffect(audioUrl) {
        isReady = false
        isPlaying = false
        positionMs = 0L
        durationMs = 0L
        loadError = null
        try {
            player.setMediaItem(MediaItem.fromUri(audioUrl))
            player.prepare()
            player.play()
        } catch (e: Exception) {
            loadError = "پخش صدا ممکن نبود"
        }
    }

    // ============ به‌روزرسانی موقعیت ============
    LaunchedEffect(
        isPlaying,
        isReady
    ) {
        while (isPlaying && isReady) {
            try {
                positionMs = player.currentPosition.toLong()
                val d = player.duration
                if (d >= 0) durationMs = d.toLong()
            } catch (_: Exception) {
            }
            delay(500.milliseconds)
        }
    }

    // ============ تمیزکاری (فقط هنگام خروج از صفحه) ============
    DisposableEffect(Unit) {
        onDispose {
            try {
                player.removeListener(listener)
            } catch (_: Exception) {
            }
            player.release()
        }
    }

    fun toggle() {
        if (!isReady) return
        try {
            if (isPlaying) player.pause() else player.play()
        } catch (_: Exception) {
        }
    }

    fun seekTo(fraction: Float) {
        if (!isReady || durationMs <= 0L) return
        try {
            val ms = (fraction * durationMs).toLong()
                .coerceIn(
                    0L,
                    durationMs
                )
            player.seekTo(ms)
            positionMs = ms
        } catch (_: Exception) {
        }
    }

    val fraction = if (durationMs > 0L) {
        (positionMs.toFloat() / durationMs).coerceIn(
            0f,
            1f
        )
    } else {
        0f
    }

    GlassCard3D(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ============ نشان ============
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
                if (!isReady && loadError == null) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = colors.primaryDark,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = if (isPlaying) {
                            Icons.Default.Pause
                        } else {
                            Icons.Default.PlayArrow
                        },
                        contentDescription = null,
                        tint = colors.primaryDark,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            // ============ دکمه پخش/توقف ============
            Mini3DButton(
                imageVector = if (isPlaying) {
                    Icons.Default.Pause
                } else {
                    Icons.Default.PlayArrow
                },
                tint = if (isReady) colors.gold else colors.textMuted,
                onClick = { toggle() })

            Spacer(Modifier.width(14.dp))

            // ============ اطلاعات + اسلایدر ============
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🎧 ویس شعر",
                        color = colors.textPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isReady) {
                            "${formatVoiceTime(positionMs)} / ${formatVoiceTime(durationMs)}"
                        } else {
                            "—"
                        },
                        color = colors.textMuted,
                        fontSize = 12.sp
                    )
                }

                Spacer(Modifier.height(6.dp))

                // ============ اسلایدر جلو/عقب ============
                Slider(
                    value = fraction,
                    onValueChange = { seekTo(it) },
                    enabled = isReady,
                    colors = SliderDefaults.colors(
                        thumbColor = colors.gold,
                        activeTrackColor = colors.gold,
                        inactiveTrackColor = colors.surfaceGlass
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                )
            }
        }

        // ============ خطا ============
        loadError?.let {
            Text(
                text = "⚠️ $it",
                color = colors.delete,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 14.dp,
                        end = 14.dp,
                        bottom = 10.dp
                    )
            )
        }
    }
}

// ============ فرمت زمان mm:ss ============
internal fun formatVoiceTime(ms: Long): String {
    val totalSec = (ms / 1000L).toInt()
    return String.format(
        Locale.US,
        "%d:%02d",
        totalSec / 60,
        totalSec % 60
    )
}

@Preview
@Composable
fun VoicePlayerCardPreview() {
    VoicePlayerCard(
        audioUrl = ""
    )
}
