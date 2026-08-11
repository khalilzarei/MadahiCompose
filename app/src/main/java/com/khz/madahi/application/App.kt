// application/App.kt
package com.khz.madahi.application

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.khz.madahi.data.local.preferences.PreferencesManager

class App : Application() {

    companion object {
        private lateinit var instance: App

        fun getInstance(): App = instance

        fun getContext(): Context = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // تنظیم Theme بر اساس Preferences
        applyTheme()
    }

    private fun applyTheme() {
        val preferencesManager = PreferencesManager(this)
        val isNightMode = preferencesManager.isNightMode

        AppCompatDelegate.setDefaultNightMode(
            if (isNightMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}