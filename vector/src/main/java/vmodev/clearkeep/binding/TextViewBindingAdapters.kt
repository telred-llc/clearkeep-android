package vmodev.clearkeep.binding

import android.databinding.BindingAdapter
import android.widget.TextView

interface TextViewBindingAdapters {
    @BindingAdapter(value = ["timeStamp"], requireAll = false)
    fun bindTime(textView: TextView, timeStamp: Long?)
}