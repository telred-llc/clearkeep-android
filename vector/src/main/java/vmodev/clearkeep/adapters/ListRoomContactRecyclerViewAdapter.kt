package vmodev.clearkeep.adapters

import android.arch.lifecycle.LifecycleOwner
import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.AsyncDifferConfig
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import im.vector.R
import im.vector.databinding.ItemConversationBinding
import im.vector.databinding.RoomInviteItemBinding
import im.vector.databinding.RoomItemBinding
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.RoomListUser

class ListRoomContactRecyclerViewAdapter constructor(appExecutors: AppExecutors, diffCallback: DiffUtil.ItemCallback<RoomListUser>)

    : ListAdapter<RoomListUser, DataBoundViewHolder<ItemConversationBinding>>(AsyncDifferConfig.Builder<RoomListUser>(diffCallback)
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()), IListRoomRecyclerViewAdapter {

    private lateinit var itemClick: (RoomListUser, Int) -> Unit?
    private lateinit var itemLongClick: (RoomListUser) -> Unit?
    private lateinit var dataBindingComponent: DataBindingComponent
    private var callbackToGetUsers: IListRoomRecyclerViewAdapter.ICallbackToGetUsers? = null;
    private var lifecycleOwner: LifecycleOwner? = null;
    private var currentUserId: String? = null;

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DataBoundViewHolder<ItemConversationBinding> {
        val binding: ItemConversationBinding = DataBindingUtil.inflate(LayoutInflater.from(p0.context), R.layout.item_conversation, p0, false, dataBindingComponent);
//        binding.root.setOnClickListener { binding.room?.let { itemClick?.invoke(it, 0) } }

        return DataBoundViewHolder(binding);
    }

    override fun setCallbackToGetUsers(callback: IListRoomRecyclerViewAdapter.ICallbackToGetUsers, lifecycleOwner: LifecycleOwner, currentUserId: String?) {
        callbackToGetUsers = callback;
        this.lifecycleOwner = lifecycleOwner;
        this.currentUserId = currentUserId;
    }

    override fun onBindViewHolder(p0: DataBoundViewHolder<ItemConversationBinding>, p1: Int) {
//        p0.binding.room = getItem(p1);
//        p0.binding.executePendingBindings();
//        callbackToGetUsers?.let {
//            p0.binding.memberUsers = it.getUsers(getItem(p1).id);
//            p0.binding.currentUserId = currentUserId;
//        }
    }

    override fun setOnItemClick(itemClick: (RoomListUser, Int) -> Unit?) {
        this.itemClick = itemClick;
    }

    override fun setOnItemLongClick(itemLongClick: (RoomListUser) -> Unit?) {
        this.itemLongClick = itemLongClick;
    }

    override fun getAdapter(): ListAdapter<RoomListUser, *> {
        return this;
    }

    override fun setDataBindingComponent(dataBindingComponent: DataBindingComponent) {
        this.dataBindingComponent = dataBindingComponent;
    }
}