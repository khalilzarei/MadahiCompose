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
        set(value) = prefs.edit()
            .putBoolean(
                IS_LOGGED_IN,
                value
            )
            .apply()

    var isFirstTimeLaunch: Boolean
        get() = prefs.getBoolean(
            IS_FIRST_TIME_LAUNCH,
            true
        )
        set(value) = prefs.edit()
            .putBoolean(
                IS_FIRST_TIME_LAUNCH,
                value
            )
            .apply()

    var isNightMode: Boolean
        get() = prefs.getBoolean(
            IS_NIGHT_MODE,
            false
        )
        set(value) = prefs.edit()
            .putBoolean(
                IS_NIGHT_MODE,
                value
            )
            .apply()

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
            prefs.edit()
                .putString(
                    USER_KEY,
                    json
                )
                .apply()
        }

    var font: String
        get() = prefs.getString(
            KEY_FONT,
            DEFAULT_FONT
        )
                ?: DEFAULT_FONT
        set(value) = prefs.edit()
            .putString(
                KEY_FONT,
                value
            )
            .apply()

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

    // ✅ اندازه فونت برای نمایش محتوا (ذخیره به صورت Float)
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
    fun clearSession() {
        prefs.edit {
            clear()
        }
    }

    // ============ Constants ============
    companion object {
        private const val PREF_NAME = "MadahiPrefs"
        private const val IS_FIRST_TIME_LAUNCH = "is_first_time"
        private const val IS_NIGHT_MODE = "is_night_mode"
        private const val IS_LOGGED_IN = "is_logged_in"
        private const val KEY_FONT = "font_key"
        private const val KEY_FONT_SIZE = "font_size_key"
        private const val KEY_CONTENT_FONT_SIZE = "content_font_size_key"  // ✅ جدید
        private const val USER_KEY = "user_key"
        private const val DEFAULT_FONT = "vazir.ttf"
        private const val DEFAULT_FONT_SIZE = 16
    }
}