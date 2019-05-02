package vmodev.clearkeep.adapters

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

open class DataBoundViewHolder<out T : ViewDataBinding> constructor(val binding: T)
    : RecyclerView.ViewHolder(binding.root)