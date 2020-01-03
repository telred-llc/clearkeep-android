package vmodev.clearkeep.ultis

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import vmodev.clearkeep.fragments.SettingDoNotDisturbFragment
import java.text.SimpleDateFormat
import java.util.*

class DateUtil {
    companion object {
        fun isNotificationActive(context: Context): Boolean {
            val timeFrom = SharedPreferencesUtils.getString(context, SettingDoNotDisturbFragment.KEY_SHARE_PREFERENCES_TIME_FROM, "00:00")?.let { getTimeOfDayByHourAndMinute(it) }
            val toFrom = SharedPreferencesUtils.getString(context, SettingDoNotDisturbFragment.KEY_SHARE_PREFERENCES_TIME_TO, "00:00")?.let { getTimeOfDayByHourAndMinute(it) }
            val timeNow = Calendar.getInstance().timeInMillis
            return timeNow >= timeFrom!! && timeNow <= toFrom!!

        }

        @SuppressLint("SimpleDateFormat")
        private fun getTimeOfDayByHourAndMinute(time: String): Long {
            val calendar: Calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm")
            val currentDateandTime = sdf.format(Date())
            val date = sdf.parse(currentDateandTime)
            calendar.time = date
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            if (!TextUtils.isEmpty(time)) {
                val timeDetachList = time.split(":")
                if (timeDetachList.size > 1) {
                    calendar.add(Calendar.HOUR, timeDetachList[0].toInt())
                    calendar.add(Calendar.MINUTE, timeDetachList[1].toInt())
                }
            }
            return calendar.timeInMillis
        }
    }
}