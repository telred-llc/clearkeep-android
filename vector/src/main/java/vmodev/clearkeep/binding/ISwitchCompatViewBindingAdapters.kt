package vmodev.clearkeep.binding

import android.databinding.BindingAdapter
import android.support.v7.widget.SwitchCompat

interface ISwitchCompatViewBindingAdapters {
    @BindingAdapter(value = ["status"], requireAll = false)
    fun bindStatus(switchCompat: SwitchCompat, status: Byte?)
}