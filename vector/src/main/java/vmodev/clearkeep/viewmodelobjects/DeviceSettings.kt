package vmodev.clearkeep.viewmodelobjects

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"])
data class DeviceSettings(
        @field:SerializedName("id") val id: String,
        @field:SerializedName("notifications_on_this_device") val notificationOnThisDevice: Byte,
        @field:SerializedName("show_decrypted_content") val showDecryptedContent: Byte,
        @field:SerializedName("pin_room_with_missed_notifications") val pinRoomWithMissedNotifications: Byte,
        @field:SerializedName("pin_room_with_unread_messages") val pinRoomWithUnreadMessages: Byte,
        @field:SerializedName("integrated_calling") val integratedCalling: Byte,
        @field:SerializedName("send_anon_crash_and_usage_data") val sendAnonCrashAndUsageData: Byte,
        @field:SerializedName("rage_shake_to_report_bug") val rageShakeToReportBug: Byte,
        @field:SerializedName("theme") val theme: Int
)