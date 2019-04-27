package vmodev.clearkeep.binding

import android.databinding.BindingAdapter
import android.view.View
import android.widget.TextView
import vmodev.clearkeep.ultis.toDateTime

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }
}