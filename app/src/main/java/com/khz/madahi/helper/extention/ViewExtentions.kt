package com.khz.madahi.helper.extention

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import java.lang.Long
import kotlin.Any
import kotlin.Int
import kotlin.String
import kotlin.let


fun Context.dpToPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()

fun Fragment.dpToPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()

fun Fragment.pxToDp(px: Int): Int = (px / resources.displayMetrics.density).toInt()


fun Context.getAttrColor(attrResId: Int): Int {
    val typedValue = TypedValue()
    val theme = theme
    val got = theme.resolveAttribute(
        attrResId, typedValue, true
    )
    return if (got) typedValue.data else Color.MAGENTA // مقدار پیش‌فرض در صورت عدم وجود
}

fun getTempValue(value: Int): Int = if (value > 0) 1000000 / value else 1000000 / 2700

fun hexToDecimal(hex: String) = Long.parseLong(
    hex, 16
)

fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(
        windowToken, 0
    )
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() { // Calls Context.hideKeyboard
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    view.hideKeyboard()
}

fun Any.logD(message: String) {
    let {
        val className = it::class.simpleName
        Log.d(
            className, "$className => $message"
        )
    }
}

fun Any.logI(message: String) {
    let {
        val className = it::class.simpleName
        Log.i(
            className, "$className => $message"
        )
    }
}

fun Any.logE(message: String) {
    let {
        val className = it::class.simpleName
        Log.e(
            className, "$className => $message"
        )
    }
}

fun Any.logW(message: String) {
    let {
        val className = it::class.simpleName
        Log.w(
            className, "$className => $message"
        )
    }
}


