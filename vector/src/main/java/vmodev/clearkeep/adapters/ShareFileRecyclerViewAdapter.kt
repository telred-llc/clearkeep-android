package vmodev.clearkeep.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import im.vector.R
import im.vector.databinding.ItemRoomDirectShareFileBinding
import im.vector.databinding.RoomInviteItemBinding
import im.vector.databinding.RoomItemBinding
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.bindingadapters.interfaces.IDataBindingComponent
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.RoomListUser

class ShareFileRecyclerViewAdapter constructor(appExecutors: AppExecutors, diffCallback: DiffUtil.ItemCallback<RoomListUser>, private val dataBindingComponent : IDataBindingComponent)

    : ListAdapter<RoomListUser, DataBoundViewHolder<ViewDataBinding>>(AsyncDifferConfig.Builder(diffCallback)
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()), IListRoomRecyclerViewAdapter {

    private val layouts: Array<Int> = arrayOf(R.layout.room_invite_item, R.layout.room_item);
    private lateinit var itemClick: (RoomListUser, Int) -> Unit?
    private lateinit var itemLongClick: (RoomListUser) -> Unit?
    private var callbackToGetUsers: IListRoomRecyclerViewAdapter.ICallbackToGetUsers? = null;
    private var lifecycleOwner: LifecycleOwner? = null;
    private var currentUserId: String? = null;


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DataBoundViewHolder<ViewDataBinding> {
        val binding: ViewDataBinding;
        if (p1 == 0) {
            binding = DataBindingUtil.inflate<ItemRoomDirectShareFileBinding>(LayoutInflater.from(p0.context), R.layout.item_room_direct_share_file, p0, false, dataBindingComponent.getDataBindingComponent());
            binding.root.setOnClickListener { binding.roomListUser?.let { itemClick?.invoke(it, 0) } }
//            binding.buttonJoin.setOnClickListener { binding.roomListUser?.let { itemClick?.invoke(it, 1) } }
//            binding.buttonDecline.setOnClickListener { binding.roomListUser?.let { itemClick?.invoke(it, 2) } }
        } else {
            binding = DataBindingUtil.inflate<ItemRoomDirectShareFileBinding>(LayoutInflater.from(p0.context), R.layout.item_room_direct_share_file, p0, false, dataBindingComponent.getDataBindingComponent());
            binding.root.setOnClickListener { binding.roomListUser?.let { itemClick?.invoke(it, 3) } }
            binding.root.setOnLongClickListener { v: View? ->
                binding.roomListUser?.let { itemLongClick?.invoke(it) }
                return@setOnLongClickListener true
            }
        }
        lifecycleOwner?.let { binding.lifecycleOwner = lifecycleOwner }
        return DataBoundViewHolder(binding);
    }

    override fun onBindViewHolder(p0: DataBoundViewHolder<ViewDataBinding>, p1: Int) {
        if (getItemViewType(p1) == 0) {
            (p0.binding as RoomInviteItemBinding).roomListUser = getItem(p1);
            p0.binding.executePendingBindings();
        } else {
            (p0.binding as ItemRoomDirectShareFileBinding).roomListUser = getItem(p1);
            p0.binding.executePendingBindings();
            //p0.binding.currentUserId = currentUserId;
            callbackToGetUsers?.let { callback ->
                getItem(p1).roomUserJoin?.let {
                    val userIds = Array(it.size) { i -> it[i].userId };
                    //p0.binding. = callback.getUsers(userIds);
                }
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).room?.type != 65 && getItem(position).room?.type != 66) 1 else 0;

    }
    override fun setOnItemClick(itemClick: (RoomListUser, Int) -> Unit?) {
        this.itemClick = itemClick;
    }

    override fun setOnItemLongClick(itemLongClick: (RoomListUser) -> Unit?) {
        this.itemLongClick = itemLongClick;
    }

    override fun getAdapter(): ListAdapter<RoomListUser, *> {
        return this
    }

    override fun setCallbackToGetUsers(callback: IListRoomRecyclerViewAdapter.ICallbackToGetUsers, lifecycleOwner: LifecycleOwner, currentUserId: String?) {
        callbackToGetUsers = callback;
        this.lifecycleOwner = lifecycleOwner;
        this.currentUserId = currentUserId;
    }


}