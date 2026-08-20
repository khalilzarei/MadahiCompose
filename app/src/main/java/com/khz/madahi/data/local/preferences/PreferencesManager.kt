// data/local/preferences/PreferencesManager.kt
package com.khz.madahi.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.khz.madahi.models.User

class PreferencesManager(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREF_NAME,
        Context.MODE_PRIVATE
    )

    // ============ Properties ============
    var isLoggedIn: Boolean
        get() = prefs.getBoolean(
            IS_LOGGED_IN,
            false
        )
        set(value) = prefs.edit {
            putBoolean(
                IS_LOGGED_IN,
                value
            )
        }

    var isFirstTimeLaunch: Boolean
        get() = prefs.getBoolean(
            IS_FIRST_TIME_LAUNCH,
            true
        )
        set(value) = prefs.edit {
            putBoolean(
                IS_FIRST_TIME_LAUNCH,
                value
            )
        }

    var isNightMode: Boolean
        get() = prefs.getBoolean(
            IS_NIGHT_MODE,
            true
        )
        set(value) = prefs.edit {
            putBoolean(
                IS_NIGHT_MODE,
                value
            )
        }

    // ✅ توکن احراز هویت (سرور امن)
    var token: String?
        get() = prefs.getString(
            KEY_TOKEN,
            null
        )
        set(value) = prefs.edit {
            if (value == null) remove(KEY_TOKEN) else putString(
                KEY_TOKEN,
                value
            )
        }

    var user: User?
        get() {
            val json = prefs.getString(
                USER_KEY,
                null
            )
                    ?: return null
            return try {
                Gson().fromJson(
                    json,
                    User::class.java
                )
            } catch (e: Exception) {
                null
            }
        }
        set(value) {
            val json = Gson().toJson(value)
            prefs.edit {
                putString(
                    USER_KEY,
                    json
                )
            }
        }

    var font: String
        get() = prefs.getString(
            KEY_FONT,
            DEFAULT_FONT
        )
                ?: DEFAULT_FONT
        set(value) = prefs.edit {
            putString(
                KEY_FONT,
                value
            )
        }

    var fontSize: Int
        get() = prefs.getInt(
            KEY_FONT_SIZE,
            DEFAULT_FONT_SIZE
        )
        set(value) = prefs.edit {
            putInt(
                KEY_FONT_SIZE,
                value
            )
        }

    // اندازه فونت برای نمایش محتوا (Float)
    var contentFontSize: Float
        get() = prefs.getFloat(
            KEY_CONTENT_FONT_SIZE,
            16f
        )
        set(value) = prefs.edit {
            putFloat(
                KEY_CONTENT_FONT_SIZE,
                value
            )
        }

    // ============ Methods ============
    /**
     * خروج از حساب — فقط اطلاعات نشست پاک می‌شود
     * (تم، فونت و تنظیمات دست نمی‌خورد)
     */
    fun clearSession() {
        prefs.edit {
            remove(IS_LOGGED_IN)
            remove(USER_KEY)
            remove(KEY_TOKEN)
        }
    }

    // ============ Constants ============
    companion object {
        private const val PREF_NAME = "MadahiPrefs"
        private const val IS_FIRST_TIME_LAUNCH = "is_first_time"
        private const val IS_NIGHT_MODE = "is_night_mode"
        private const val IS_LOGGED_IN = "is_logged_in"
        private const val KEY_TOKEN = "token_key"          // ✅ جدید
        private const val KEY_FONT = "font_key"
        private const val KEY_FONT_SIZE = "font_size_key"
        private const val KEY_CONTENT_FONT_SIZE = "content_font_size_key"
        private const val USER_KEY = "user_key"
        private const val DEFAULT_FONT = "vazir.ttf"
        private const val DEFAULT_FONT_SIZE = 16
    }
}
