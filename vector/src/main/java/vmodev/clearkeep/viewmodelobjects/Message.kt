package vmodev.clearkeep.viewmodelobjects

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"],
        foreignKeys = [
            ForeignKey(entity = Room::class,
                    parentColumns = ["id"],
                    childColumns = ["id"],
                    onDelete = ForeignKey.CASCADE),
            ForeignKey(entity = User::class,
                    parentColumns = ["id"],
                    childColumns = ["id"],
                    onDelete = ForeignKey.CASCADE)
        ])
data class Message(
        @field:SerializedName("id") val id: String,
        @field:SerializedName("encrypted_content") val encryptedContent: String
)