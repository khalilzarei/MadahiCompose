// ui/audio/AudioUtils.kt
package com.khz.madahi.ui.audio

import android.content.Context
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMetadataRetriever
import android.media.MediaMuxer
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.ByteBuffer

object AudioUtils {

    private const val TAG = "AudioUtils"

    const val MAX_DURATION_MS = 60_000L
    const val MAX_FILE_SIZE_BYTES = 1L * 1024 * 1024

    @Volatile
    var lastError: String? = null

    /** اگر فایل بزرگتر از حد مجاز باشد، حداکثر مدتزمانی که زیر ۱ مگابایت میماند را برمیگرداند */
    fun autoFitEndMs(
        durationMs: Long,
        fileSize: Long,
        maxSize: Long = MAX_FILE_SIZE_BYTES
    ): Long {
        if (durationMs <= 0L) return 0L
        if (fileSize <= maxSize) return minOf(
            durationMs,
            MAX_DURATION_MS
        )

        // نسبت حجم به مدت، تخمین میزنیم چه مدتزمانی زیر ۱ مگابایت باشد
        val estimatedMs = (maxSize * durationMs) / fileSize
        // حداقل ۱ ثانیه، حداکثر ۱ دقیقه
        return estimatedMs.coerceIn(
            1_000L,
            minOf(
                durationMs,
                MAX_DURATION_MS
            )
        )
    }

    fun fileSizeBytes(
        context: Context,
        uri: Uri
    ): Long {
        try {
            context.contentResolver.query(
                uri,
                null,
                null,
                null,
                null
            )
                ?.use { c ->
                    val idx = c.getColumnIndex(OpenableColumns.SIZE)
                    if (idx >= 0 && c.moveToFirst()) {
                        val s = c.getLong(idx)
                        if (s > 0L) return s
                    }
                }
        } catch (_: Exception) {
        }
        return try {
            context.contentResolver.openInputStream(uri)
                ?.use {
                    it.copyTo(ByteArrayOutputStream())
                        .toLong()
                }
                    ?: -1L
        } catch (e: Exception) {
            -1L
        }
    }

    fun durationMs(
        context: Context,
        uri: Uri
    ): Long {
        return try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(
                context,
                uri
            )
            val ms = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                ?.toLongOrNull()
                    ?: 0L
            retriever.release()
            if (ms > 0) ms else 0L
        } catch (e: Exception) {
            0L
        }
    }

    /**
     * برش فایل به بازهی [startMs, endMs].
     * اگر بازه برابر کل فایل و حجم زیر ۱ مگابایت باشد، بدون تغییر برمیگردد.
     * اگر MP3 باشد با برشزنندهی خالص کات میکند.
     * بقیه فرمتها با MediaMuxer کات میشوند.
     */
    fun trimRange(
        context: Context,
        uri: Uri,
        startMs: Long,
        endMs: Long,
        knownDurationMs: Long = 0L
    ): Uri {
        val dur = if (knownDurationMs > 0L) knownDurationMs else durationMs(
            context,
            uri
        )
        if (dur <= 0L || endMs <= startMs) {
            lastError = "زمان انتخابی نامعتبر است"
            return uri
        }

        // اگر کل فایل انتخاب شده و حجمش مجاز است، نیازی به برش نیست
        val size = fileSizeBytes(
            context,
            uri
        )
        if (startMs <= 0L && endMs >= dur && size <= MAX_FILE_SIZE_BYTES) {
            lastError = null
            return uri
        }

        val mime = context.contentResolver.getType(uri)
                ?: ""
        val isMp3 = mime == "audio/mpeg" || mime == "audio/mp3"

        // mp3 را همیشه با روش خالص کات میکنیم
        if (isMp3) {
            return trimMp3PureKotlin(
                context,
                uri,
                startMs,
                endMs
            )
                    ?: uri
        }

        // بقیه فرمتها با MediaMuxer
        try {
            val result = trimWithMediaMuxer(
                context,
                uri,
                startMs * 1000L,
                endMs * 1000L
            )
            if (result != null) return result
        } catch (e: Exception) {
            Log.e(
                TAG,
                "MediaMuxer error: ${e.message}"
            )
        }

        lastError = "برش فایل با فرمت $mime روی این دستگاه ممکن نشد — فایل MP3 انتخاب کن"
        return uri
    }

    // ---------- برش MP3 خالص ----------
    private fun trimMp3PureKotlin(
        context: Context,
        uri: Uri,
        startMs: Long,
        endMs: Long
    ): Uri? {
        val bytes = context.contentResolver.openInputStream(uri)
            ?.use { it.readBytes() }
                ?: return null
        val len = bytes.size
        var pos = 0

        // رد کردن ID3v2
        if (len > 3 && bytes[0] == 0x49.toByte() && bytes[1] == 0x44.toByte() && bytes[2] == 0x33.toByte()) {
            val size = ((bytes[6].toInt() and 0x7f) shl 21) or ((bytes[7].toInt() and 0x7f) shl 14) or ((bytes[8].toInt() and 0x7f) shl 7) or (bytes[9].toInt() and 0x7f)
            pos = 10 + size
        }

        val brMpeg1 = intArrayOf(
            0,
            32,
            40,
            48,
            56,
            64,
            80,
            96,
            112,
            128,
            160,
            192,
            224,
            256,
            320,
            0
        )
        val brMpeg2 = intArrayOf(
            0,
            8,
            16,
            24,
            32,
            40,
            48,
            56,
            64,
            80,
            96,
            112,
            128,
            144,
            160,
            0
        )
        val srMpeg1 = intArrayOf(
            44100,
            48000,
            32000,
            0
        )
        val srMpeg2 = intArrayOf(
            22050,
            24000,
            16000,
            0
        )
        val srMpeg25 = intArrayOf(
            11025,
            12000,
            8000,
            0
        )

        val startSec = startMs / 1000.0
        val endSec = endMs / 1000.0
        var t = 0.0
        var firstFrameSkipped = false
        val out = ByteArrayOutputStream()

        while (pos + 4 <= len) {
            if ((bytes[pos].toInt() and 0xFF) != 0xFF || (bytes[pos + 1].toInt() and 0xE0) != 0xE0) {
                pos++
                continue
            }
            val h1 = bytes[pos + 1].toInt()
            val h2 = bytes[pos + 2].toInt()
            val version = (h1 shr 3) and 0x03
            val layer = (h1 shr 1) and 0x03
            if (layer != 1 || version == 1) {
                pos++; continue
            }

            val brIdx = (h2 shr 4) and 0x0F
            val srIdx = (h2 shr 2) and 0x03
            val pad = (h2 shr 1) and 0x01
            if (brIdx == 0 || brIdx == 15 || srIdx == 3) {
                pos++; continue
            }

            val bitrate = if (version == 3) brMpeg1[brIdx] else brMpeg2[brIdx]
            val sampleRate = when (version) {
                3 -> srMpeg1[srIdx]
                2 -> srMpeg2[srIdx]
                else -> srMpeg25[srIdx]
            }
            if (bitrate == 0 || sampleRate == 0) {
                pos++; continue
            }

            val frameSize = if (version == 3) {
                (bitrate * 1000 * 144) / sampleRate + pad
            } else {
                (bitrate * 1000 * 72) / sampleRate + pad
            }
            val frameDur = if (version == 3) 1152.0 / sampleRate else 576.0 / sampleRate
            if (frameSize < 8 || pos + frameSize > len) break

            if (!firstFrameSkipped) {
                firstFrameSkipped = true
                t += frameDur
                pos += frameSize
                continue
            }

            if (t >= endSec) break
            if (t >= startSec) out.write(
                bytes,
                pos,
                frameSize
            )
            t += frameDur
            pos += frameSize
        }

        if (out.size() < 100) return null

        val cacheDir = context.getExternalFilesDir(null)
                ?: context.cacheDir
        val mp3File = File(
            cacheDir,
            "trim_${System.currentTimeMillis()}.mp3"
        )
        mp3File.writeBytes(out.toByteArray())
        return Uri.fromFile(mp3File)
    }

    // ---------- برش بقیه فرمتها با MediaMuxer ----------
    private fun trimWithMediaMuxer(
        context: Context,
        uri: Uri,
        startUs: Long,
        endUs: Long
    ): Uri? {
        var extractor: MediaExtractor? = null
        var muxer: MediaMuxer? = null
        return try {
            extractor = MediaExtractor()
            extractor.setDataSource(
                context,
                uri,
                null
            )

            var trackIndex = -1
            for (i in 0 until extractor.trackCount) {
                val mime = extractor.getTrackFormat(i)
                    .getString(MediaFormat.KEY_MIME)
                        ?: ""
                if (mime.startsWith("audio/")) {
                    trackIndex = i
                    break
                }
            }
            if (trackIndex < 0) return null

            val cacheDir = context.getExternalFilesDir(null)
                    ?: context.cacheDir
            val outputFile = File(
                cacheDir,
                "trim_${System.currentTimeMillis()}.m4a"
            )
            muxer = MediaMuxer(
                outputFile.absolutePath,
                MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4
            )

            extractor.selectTrack(trackIndex)
            val format = extractor.getTrackFormat(trackIndex)
            val outTrack = muxer.addTrack(format)
            muxer.start()
            extractor.seekTo(
                startUs,
                MediaExtractor.SEEK_TO_PREVIOUS_SYNC
            )

            val buffer = ByteBuffer.allocate(256 * 1024)
            val info = MediaCodec.BufferInfo()
            var writtenAny = false

            while (true) {
                val size = extractor.readSampleData(
                    buffer,
                    0
                )
                if (size < 0) break
                val sampleTimeUs = extractor.sampleTime
                if (sampleTimeUs == -1L) {
                    extractor.advance()
                    continue
                }
                if (sampleTimeUs > endUs) break

                info.offset = 0
                info.size = size
                info.flags = extractor.sampleFlags
                info.presentationTimeUs = (sampleTimeUs - startUs).coerceAtLeast(0L)

                muxer.writeSampleData(
                    outTrack,
                    buffer,
                    info
                )
                writtenAny = true
                extractor.advance()
            }

            muxer.stop()
            muxer.release()
            muxer = null
            extractor.release()
            extractor = null

            if (writtenAny && outputFile.exists() && outputFile.length() > 0) {
                return Uri.fromFile(outputFile)
            } else {
                outputFile.delete()
                return null
            }
        } catch (e: Exception) {
            Log.e(
                TAG,
                "MediaMuxer failed",
                e
            )
            try {
                muxer?.stop()
            } catch (_: Exception) {
            }
            try {
                muxer?.release()
            } catch (_: Exception) {
            }
            try {
                extractor?.release()
            } catch (_: Exception) {
            }
            return null
        }
    }
}