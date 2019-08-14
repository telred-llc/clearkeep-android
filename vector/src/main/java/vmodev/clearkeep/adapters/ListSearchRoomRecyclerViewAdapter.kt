package vmodev.clearkeep.adapters

import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.support.v7.recyclerview.extensions.AsyncDifferConfig
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import im.vector.R
import im.vector.databinding.ItemMessageSearchBinding
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.MessageRoomUser

class ListSearchRoomRecyclerViewAdapter constructor(appExecutors: AppExecutors, diffCallback: DiffUtil.ItemCallback<MessageRoomUser>
                                                    , private val dataBindingComponent: DataBindingComponent
                                                    , private val itemClick: (MessageRoomUser) -> Unit?)
    : ListAdapter<MessageRoomUser, DataBoundViewHolder<ItemMessageSearchBinding>>(AsyncDifferConfig.Builder(diffCallback)
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()) {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DataBoundViewHolder<ItemMessageSearchBinding> {
        val binding = DataBindingUtil.inflate<ItemMessageSearchBinding>(LayoutInflater.from(p0.context), R.layout.item_message_search, p0, false, dataBindingComponent);
        binding.root.setOnClickListener { v ->
            binding.result?.let {
                itemClick?.invoke(it)
            }
        }
        return DataBoundViewHolder(binding);
    }

    override fun onBindViewHolder(p0: DataBoundViewHolder<ItemMessageSearchBinding>, p1: Int) {
        p0.binding.result = getItem(p1);
    }
}