// ui/audio/AudioRecorder.kt
package com.khz.madahi.ui.audio

import android.content.Context
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import java.io.File

/**
 * 🎙️ ضبط‌کننده‌ی ویس با سقف یک دقیقه
 * ------------------------------------------------------------
 * - خروجی AAC داخل کانتینر M4A (بیت‌ریت 64k ≈ 0.5 مگابایت در دقیقه)
 *   تا حجم فایل کم بماند و سرور سنگین نشود
 * - فایل در cacheDir اپ ذخیره می‌شود (موقتی)
 * - زمان‌بندی قطع خودکار روی ۶۰ ثانیه سمت دیالوگ اعمال می‌شود
 */
class AudioRecorder(private val context: Context) {

    companion object {
        const val MAX_DURATION_MS = 60_000L
        private const val BIT_RATE = 64_000   // 64 kbps
        private const val SAMPLE_RATE = 44_100
    }

    private var recorder: MediaRecorder? = null
    private var outputFile: File? = null

    val isRecording: Boolean
        get() = recorder != null

    /**
     * شروع ضبط — true اگر موفق بود
     */
    fun start(): Boolean {
        if (recorder != null) return false
        return try {
            // ⚠️ فایل در دیتابیس خصوصی اپ (cacheDir) ذخیره نمی‌شود:
            // پلیر سیستمی روی دستگاه‌های Rockchip (TV Box) فرآیند جدا دارد
            // و به /data/data/... دسترسی ندارد → از external files dir استفاده می‌شود
            val dir = context.getExternalFilesDir(null)
                    ?: context.cacheDir
            val file = File(
                dir,
                "rec_${System.currentTimeMillis()}.m4a"
            )

            @Suppress("DEPRECATION")
            val r = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                MediaRecorder()
            }
            r.setAudioSource(MediaRecorder.AudioSource.MIC)
            r.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            r.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            r.setAudioSamplingRate(SAMPLE_RATE)
            r.setAudioEncodingBitRate(BIT_RATE)
            r.setOutputFile(file.absolutePath)
            r.prepare()
            r.start()
            recorder = r
            outputFile = file
            true
        } catch (e: Exception) {
            cleanup()
            false
        }
    }

    /**
     * توقف ضبط و برگشتن Uri فایل — اگر ضبط معتبر نبود، null
     */
    fun stop(): Uri? {
        val rec = recorder
        try {
            rec?.stop()
        } catch (e: Exception) {
            // ضبط ناقص/خیلی کوتاه — نادیده بگیر
        } finally {
            try {
                rec?.release()
            } catch (_: Exception) {
            }
            recorder = null
        }
        val f = outputFile
        outputFile = null
        if (f == null || !f.exists() || f.length() == 0L) {
            f?.delete()
            return null
        }
        return Uri.fromFile(f)
    }

    /**
     * لغو ضبط و حذف فایل
     */
    fun cancel() {
        try {
            recorder?.stop()
        } catch (_: Exception) {
        }
        try {
            recorder?.release()
        } catch (_: Exception) {
        }
        recorder = null
        outputFile?.delete()
        outputFile = null
    }

    private fun cleanup() {
        try {
            recorder?.release()
        } catch (_: Exception) {
        }
        recorder = null
        outputFile?.delete()
        outputFile = null
    }
}
