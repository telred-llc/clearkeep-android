package vmodev.clearkeep.ultis

import android.content.Context
import android.text.format.DateUtils
import im.vector.R
import im.vector.util.PreferencesManager
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

    fun Long.toDateTime(context: Context, timeOnly: Boolean = false): String {
        val MS_IN_DAY = (1000 * 60 * 60 * 24).toLong()
        val daysDiff = (Date().time - (Date(this)).zeroTimeDate().time) / MS_IN_DAY

        val res: String

        if (timeOnly) {
            res = DateUtils.formatDateTime(context, this,
                DateUtils.FORMAT_SHOW_TIME or getTimeDisplay(context))
        } else if (0L == daysDiff) {
            res = context.getString(R.string.today) + " " + DateUtils.formatDateTime(context, this,
                DateUtils.FORMAT_SHOW_TIME or getTimeDisplay(context))
        } else if (1L == daysDiff) {
            res = context.getString(R.string.yesterday) + " " + DateUtils.formatDateTime(context, this,
                DateUtils.FORMAT_SHOW_TIME or getTimeDisplay(context))
        } else if (7 > daysDiff) {
            res = DateUtils.formatDateTime(context, this,
                DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_SHOW_WEEKDAY or DateUtils.FORMAT_ABBREV_ALL or getTimeDisplay(context))
        } else if (365 > daysDiff) {
            res = DateUtils.formatDateTime(context, this,
                DateUtils.FORMAT_ABBREV_ALL or DateUtils.FORMAT_SHOW_DATE)
        } else {
            res = DateUtils.formatDateTime(context, this,
                DateUtils.FORMAT_ABBREV_ALL or DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)
        }

        return res
    }

    fun Date.zeroTimeDate(): Date {
        val gregorianCalendar = GregorianCalendar()
        gregorianCalendar.time = this
        gregorianCalendar.set(Calendar.HOUR_OF_DAY, 0)
        gregorianCalendar.set(Calendar.MINUTE, 0)
        gregorianCalendar.set(Calendar.SECOND, 0)
        gregorianCalendar.set(Calendar.MILLISECOND, 0)
        return gregorianCalendar.time
    }

    private fun getTimeDisplay(context: Context): Int {
        return if (PreferencesManager.displayTimeIn12hFormat(context)) DateUtils.FORMAT_12HOUR else DateUtils.FORMAT_24HOUR
    }
