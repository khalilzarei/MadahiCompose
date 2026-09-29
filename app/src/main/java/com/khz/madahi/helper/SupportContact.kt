package com.khz.madahi.helper

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

const val SUPPORT_PHONE_NUMBER = "09362371808"

fun openSupportContact(context: Context) {
    val dialIntent = Intent(
        Intent.ACTION_DIAL,
        Uri.fromParts(
            "tel",
            SUPPORT_PHONE_NUMBER,
            null
        )
    )

    try {
        context.startActivity(dialIntent)
    } catch (_: ActivityNotFoundException) {
        copySupportNumber(context)
    } catch (_: SecurityException) {
        copySupportNumber(context)
    }
}

private fun copySupportNumber(context: Context) {
    try {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(
            ClipData.newPlainText(
                "شماره پشتیبانی",
                SUPPORT_PHONE_NUMBER
            )
        )
        Toast.makeText(
            context,
            "شماره پشتیبانی کپی شد: $SUPPORT_PHONE_NUMBER",
            Toast.LENGTH_LONG
        )
            .show()
    } catch (_: Exception) {
        Toast.makeText(
            context,
            "شماره پشتیبانی: $SUPPORT_PHONE_NUMBER",
            Toast.LENGTH_LONG
        )
            .show()
    }
}
