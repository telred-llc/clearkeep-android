package vmodev.clearkeep.adapters

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


open class DataBoundViewHolder<out T : ViewDataBinding> constructor(val binding: T)
    : RecyclerView.ViewHolder(binding.root)