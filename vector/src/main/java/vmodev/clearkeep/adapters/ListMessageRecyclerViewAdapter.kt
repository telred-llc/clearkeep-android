package vmodev.clearkeep.adapters

import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.AsyncDifferConfig
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import im.vector.R
import im.vector.databinding.ItemMessageBinding
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.Message

class ListMessageRecyclerViewAdapter constructor(appExecutors: AppExecutors, private val dataBindingComponent: DataBindingComponent, diffCall: DiffUtil.ItemCallback<Message>)
    : ListAdapter<Message, DataBoundViewHolder<ItemMessageBinding>>(AsyncDifferConfig.Builder<Message>(diffCall)
        .setBackgroundThreadExecutor(appExecutors.diskIO()).build()) {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DataBoundViewHolder<ItemMessageBinding> {
        val binding = DataBindingUtil.inflate<ItemMessageBinding>(LayoutInflater.from(p0.context), R.layout.item_message, p0, false, dataBindingComponent);
        return DataBoundViewHolder(binding);
    }

    override fun onBindViewHolder(p0: DataBoundViewHolder<ItemMessageBinding>, p1: Int) {
        p0.binding.message = getItem(p1);
    }
}