package vmodev.clearkeep.binding

import android.databinding.BindingAdapter
import android.widget.TextView
import vmodev.clearkeep.viewmodelobjects.Room

interface TextViewBindingAdapters {
    @BindingAdapter(value = ["timeStamp"], requireAll = false)
    fun bindTime(textView: TextView, timeStamp: Long?)
    @BindingAdapter(value = ["roomsHighlightCount"], requireAll = false)
    fun bindRoomsHighlightCount (textView: TextView, rooms : List<Room>?)
}