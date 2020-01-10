package vmodev.clearkeep.bindingadapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import org.matrix.androidsdk.rest.model.message.ImageMessage
import vmodev.clearkeep.viewmodelobjects.Message
import vmodev.clearkeep.viewmodelobjects.Room

interface TextViewBindingAdapters {
    @BindingAdapter(value = ["timeStamp"], requireAll = false)
    fun bindTime(textView: TextView, timeStamp: Long?)

    @BindingAdapter(value = ["roomsHighlightCount"], requireAll = false)
    fun bindRoomsHighlightCount(textView: TextView, rooms: List<Room>?)

    @BindingAdapter(value = ["decryptMessage"], requireAll = false)
    fun bindDecryptMessage(textView: TextView, message: Message?)

    @BindingAdapter(value = ["formatName"], requireAll = false)
    fun bindFormatName(textView: TextView, room: Room?)

    @BindingAdapter(value = ["checkMissCall"], requireAll = false)
    fun bindCheckMissCall(textView: TextView, message: Message?)

    @BindingAdapter(value = ["dataSize"], requireAll = false)
    fun bindDataSize(textView: TextView, fileContent: ImageMessage?)

    @BindingAdapter(value = ["userNumber"], requireAll = false)
    fun bindUserNumber(textView: TextView, userNumber: Int?)
}