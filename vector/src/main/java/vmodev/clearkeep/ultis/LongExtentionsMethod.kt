package vmodev.clearkeep.ultis

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun Long.longTimeToString(): String {
    val hourMinSecFormat = SimpleDateFormat("HH:mm:ss")
    hourMinSecFormat.timeZone = TimeZone.getTimeZone("UTC")

    val minSecFormat = SimpleDateFormat("mm:ss")
    minSecFormat.timeZone = TimeZone.getTimeZone("UTC")

    return if (this < 3600) {
        minSecFormat.format(Date(this * 1000))
    } else {
        hourMinSecFormat.format(Date(this * 1000))
    }
}