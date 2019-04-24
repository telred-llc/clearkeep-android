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
import im.vector.databinding.RoomItemBinding
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.Room

class ListRoomRecyclerViewAdapter constructor(appExecutors: AppExecutors, diffCallback: DiffUtil.ItemCallback<Room>
                                              , private val dataBindingComponent: DataBindingComponent
                                              , private val itemClick: (Room) -> Unit?)
    : ListAdapter<Room, DataBoundViewHolder<RoomItemBinding>>(AsyncDifferConfig.Builder<Room>(diffCallback)
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()) {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DataBoundViewHolder<RoomItemBinding> {
        val binding = DataBindingUtil.inflate<RoomItemBinding>(LayoutInflater.from(p0.context), R.layout.room_item, p0, false, dataBindingComponent);
        binding.root.setOnClickListener { binding.room?.let { itemClick?.invoke(it) } }
        return DataBoundViewHolder(binding);
    }

    override fun onBindViewHolder(p0: DataBoundViewHolder<RoomItemBinding>, p1: Int) {
        p0.binding.room = getItem(p1);
        p0.binding.executePendingBindings();
    }
}