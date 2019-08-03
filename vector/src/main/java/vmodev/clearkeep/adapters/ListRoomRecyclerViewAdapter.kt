package vmodev.clearkeep.adapters

import android.arch.lifecycle.LifecycleOwner
import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.AsyncDifferConfig
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import im.vector.R
import im.vector.databinding.RoomInviteItemBinding
import im.vector.databinding.RoomItemBinding
import io.reactivex.subjects.PublishSubject
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.RoomListUser

class ListRoomRecyclerViewAdapter constructor(appExecutors: AppExecutors, diffCallback: DiffUtil.ItemCallback<RoomListUser>)

    : ListAdapter<RoomListUser, DataBoundViewHolder<ViewDataBinding>>(AsyncDifferConfig.Builder(diffCallback)
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()), IListRoomRecyclerViewAdapter {

    private val layouts: Array<Int> = arrayOf(R.layout.room_invite_item, R.layout.room_item);
    private lateinit var itemClick: (RoomListUser, Int) -> Unit?
    private lateinit var itemLongClick: (RoomListUser) -> Unit?
    private lateinit var dataBindingComponent: DataBindingComponent
    private var callbackToGetUsers: IListRoomRecyclerViewAdapter.ICallbackToGetUsers? = null;
    private var lifecycleOwner: LifecycleOwner? = null;
    private var currentUserId: String? = null;

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DataBoundViewHolder<ViewDataBinding> {
        val binding: ViewDataBinding;
        if (p1 == 0) {
            binding = DataBindingUtil.inflate<RoomInviteItemBinding>(LayoutInflater.from(p0.context), layouts[p1], p0, false, dataBindingComponent);
            binding.root.setOnClickListener { binding.roomListUser?.let { itemClick?.invoke(it, 0) } }
            binding.buttonJoin.setOnClickListener { binding.roomListUser?.let { itemClick?.invoke(it, 1) } }
            binding.buttonDecline.setOnClickListener { binding.roomListUser?.let { itemClick?.invoke(it, 2) } }
        } else {
            binding = DataBindingUtil.inflate<RoomItemBinding>(LayoutInflater.from(p0.context), layouts[p1], p0, false, dataBindingComponent);
            binding.root.setOnClickListener { binding.roomListUser?.let { itemClick?.invoke(it, 3) } }
            binding.root.setOnLongClickListener { v: View? ->
                binding.roomListUser?.let { itemLongClick?.invoke(it) }
                return@setOnLongClickListener true
            }
        }
        lifecycleOwner?.let { binding.lifecycleOwner = lifecycleOwner }
        return DataBoundViewHolder(binding);
    }

    override fun setCallbackToGetUsers(callback: IListRoomRecyclerViewAdapter.ICallbackToGetUsers, lifecycleOwner: LifecycleOwner, currentUserId: String?) {
        callbackToGetUsers = callback;
        this.lifecycleOwner = lifecycleOwner;
        this.currentUserId = currentUserId;
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).room?.get(0)?.type != 65 && getItem(position).room?.get(0)?.type != 66) 1 else 0;
    }

    override fun onBindViewHolder(p0: DataBoundViewHolder<ViewDataBinding>, p1: Int) {
        if (getItemViewType(p1) == 0) {
            (p0.binding as RoomInviteItemBinding).roomListUser = getItem(p1);
            p0.binding.executePendingBindings();
        } else {
            (p0.binding as RoomItemBinding).roomListUser = getItem(p1);
            p0.binding.executePendingBindings();
            p0.binding.currentUserId = currentUserId;
            callbackToGetUsers?.let {
//                p0.binding.usersMember = it.getUsers(getItem(p1).id);
            }
        }
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