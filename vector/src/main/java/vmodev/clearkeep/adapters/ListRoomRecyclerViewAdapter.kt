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
import im.vector.databinding.RoomInviteItemBinding
import im.vector.databinding.RoomItemBinding
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.Room

class ListRoomRecyclerViewAdapter constructor(appExecutors: AppExecutors, diffCallback: DiffUtil.ItemCallback<Room>
                                              , private val dataBindingComponent: DataBindingComponent
                                              , private val itemClick: (Room, Int) -> Unit?)
    : ListAdapter<Room, DataBoundViewHolder<ViewDataBinding>>(AsyncDifferConfig.Builder<Room>(diffCallback)
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()) {

    private val layouts: Array<Int> = arrayOf(R.layout.room_invite_item, R.layout.room_item);

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DataBoundViewHolder<ViewDataBinding> {
        val binding: ViewDataBinding;
        if (p1 == 0) {
            binding = DataBindingUtil.inflate<RoomInviteItemBinding>(LayoutInflater.from(p0.context), layouts[p1], p0, false, dataBindingComponent);
            binding.root.setOnClickListener { binding.room?.let { itemClick?.invoke(it, 0) } }
            binding.buttonJoin.setOnClickListener { binding.room?.let{itemClick?.invoke(it, 1)} }
            binding.buttonDecline.setOnClickListener { binding.room?.let{itemClick?.invoke(it, 2)} }
        } else {
            binding = DataBindingUtil.inflate<RoomItemBinding>(LayoutInflater.from(p0.context), layouts[p1], p0, false, dataBindingComponent);
            binding.root.setOnClickListener { binding.room?.let { itemClick?.invoke(it, 3) } }
        }

        return DataBoundViewHolder(binding);
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).type != 1 && getItem(position).type != 2) 0 else 1;
    }

    override fun onBindViewHolder(p0: DataBoundViewHolder<ViewDataBinding>, p1: Int) {
        if (getItemViewType(p1) == 0) {
            (p0.binding as RoomInviteItemBinding).room = getItem(p1);
            p0.binding.executePendingBindings();
        } else {
            (p0.binding as RoomItemBinding).room = getItem(p1);
            p0.binding.executePendingBindings();
        }
    }
}