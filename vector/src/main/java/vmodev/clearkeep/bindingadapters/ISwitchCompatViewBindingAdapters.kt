package vmodev.clearkeep.bindingadapters

import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.BindingAdapter

interface ISwitchCompatViewBindingAdapters {
    @BindingAdapter(value = ["status"], requireAll = false)
    fun bindStatus(switchCompat: SwitchCompat, status: Byte?)
}